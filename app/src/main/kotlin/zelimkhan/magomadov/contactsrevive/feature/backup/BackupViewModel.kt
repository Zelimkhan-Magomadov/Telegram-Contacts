package zelimkhan.magomadov.contactsrevive.feature.backup

import android.content.Context
import android.provider.ContactsContract
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import zelimkhan.magomadov.contactsrevive.core.database.dao.BackupDao
import zelimkhan.magomadov.contactsrevive.core.database.entity.BackupEntity
import zelimkhan.magomadov.contactsrevive.feature.importing.domain.model.Contact
import zelimkhan.magomadov.contactsrevive.feature.importing.domain.usecase.ConvertContactsToVcardUseCase
import java.util.concurrent.TimeUnit

class BackupViewModel(
    private val backupDao: BackupDao,
    private val convertContactsToVcardUseCase: ConvertContactsToVcardUseCase,
    private val context: Context,
    private val workManager: WorkManager,
) : ViewModel() {

    private val _state = MutableStateFlow(BackupState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            backupDao.getAllBackups().collectLatest { backups ->
                _state.value = _state.value.copy(backups = backups)
            }
        }

        // Initial check for auto-backup worker (simplified)
        val workInfos = workManager.getWorkInfosForUniqueWork("auto_backup").get()
        _state.value = _state.value.copy(
            isAutoBackupEnabled = workInfos.any { it.state == WorkInfo.State.ENQUEUED || it.state == WorkInfo.State.RUNNING }
        )
    }

    fun toggleAutoBackup(enabled: Boolean) {
        if (enabled) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .setRequiresCharging(false)
                .build()

            val workRequest = PeriodicWorkRequestBuilder<AutoBackupWorker>(24, TimeUnit.HOURS)
                .setConstraints(constraints)
                .build()

            workManager.enqueueUniquePeriodicWork(
                "auto_backup",
                ExistingPeriodicWorkPolicy.UPDATE,
                workRequest
            )
        } else {
            workManager.cancelUniqueWork("auto_backup")
        }
        _state.value = _state.value.copy(isAutoBackupEnabled = enabled)
    }

    fun syncContactsManually() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val localContacts = getLocalContacts()
                if (localContacts.isNotEmpty()) {
                    backupDao.insertBackup(
                        BackupEntity(
                            name = "Manual Sync ${System.currentTimeMillis()}",
                            date = System.currentTimeMillis(),
                            contactCount = localContacts.size,
                            contactsJson = Json.encodeToString(localContacts)
                        )
                    )
                }
            } catch (e: Exception) {
                // handle error
            } finally {
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }

    fun deleteBackup(backup: BackupEntity) {
        viewModelScope.launch {
            backupDao.deleteBackup(backup)
        }
    }

    suspend fun restoreBackup(backup: BackupEntity): String {
        val contacts = Json.decodeFromString<List<Contact>>(backup.contactsJson)
        return convertContactsToVcardUseCase(contacts)
    }

    private fun getLocalContacts(): List<Contact> {
        val contacts = mutableListOf<Contact>()
        val cursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null, null, null, null
        )
        cursor?.use {
            val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            while (it.moveToNext()) {
                val name = it.getString(nameIndex) ?: ""
                val number = it.getString(numberIndex) ?: ""
                contacts.add(Contact(name, number))
            }
        }
        return contacts.distinctBy { it.phoneNumber }
    }
}
