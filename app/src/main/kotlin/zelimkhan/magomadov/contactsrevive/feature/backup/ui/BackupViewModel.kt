package zelimkhan.magomadov.contactsrevive.feature.backup.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import zelimkhan.magomadov.contactsrevive.core.database.dao.BackupDao
import zelimkhan.magomadov.contactsrevive.core.database.entity.BackupEntity
import zelimkhan.magomadov.contactsrevive.feature.backup.data.AutoBackupWorker
import zelimkhan.magomadov.contactsrevive.feature.backup.domain.repository.ContactsRepository
import zelimkhan.magomadov.contactsrevive.feature.importing.domain.model.Contact
import zelimkhan.magomadov.contactsrevive.feature.importing.domain.model.merge
import zelimkhan.magomadov.contactsrevive.feature.importing.domain.repository.FileRepository
import zelimkhan.magomadov.contactsrevive.feature.importing.domain.usecase.ConvertContactsToVcardUseCase
import java.util.concurrent.TimeUnit

private const val AUTO_BACKUP_TAG = "auto_backup"

class BackupViewModel(
    private val backupDao: BackupDao,
    private val contactsRepository: ContactsRepository,
    private val fileRepository: FileRepository,
    private val convertToVcard: ConvertContactsToVcardUseCase,
    private val workManager: WorkManager,
) : ViewModel() {

    private val _state = MutableStateFlow(BackupState())
    val state = _state.asStateFlow()

    private val _events = Channel<BackupEvent>()
    val events = _events.receiveAsFlow()

    init {
        viewModelScope.launch {
            backupDao.observeAll()
                .collectLatest { list -> _state.update { it.copy(backups = list) } }
        }
        refreshAutoBackupStatus()
    }

    // ── Auto-backup ───────────────────────────────────────────────────────────

    fun setAutoBackup(enabled: Boolean) {
        if (enabled) scheduleAutoBackup() else workManager.cancelUniqueWork(AUTO_BACKUP_TAG)
        _state.update { it.copy(isAutoBackupEnabled = enabled) }
    }

    private fun scheduleAutoBackup() {
        val request = PeriodicWorkRequestBuilder<AutoBackupWorker>(24, TimeUnit.HOURS)
            .setConstraints(
                Constraints.Builder().setRequiredNetworkType(NetworkType.NOT_REQUIRED).build()
            )
            .build()
        workManager.enqueueUniquePeriodicWork(
            AUTO_BACKUP_TAG,
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }

    // ── Manual sync ───────────────────────────────────────────────────────────

    fun syncNow() = viewModelScope.launch {
        _state.update { it.copy(isSyncing = true, syncMessage = null) }
        runCatching {
            val contacts = contactsRepository.getDeviceContacts()
            if (contacts.isNotEmpty()) {
                backupDao.insert(
                    BackupEntity(
                        name = "Синхронизация",
                        date = System.currentTimeMillis(),
                        contactCount = contacts.size,
                        contactsJson = Json.encodeToString(contacts),
                    )
                )
            }
        }.fold(
            onSuccess = {
                _state.update {
                    it.copy(
                        isSyncing = false,
                        syncMessage = SyncMessage.Success
                    )
                }
            },
            onFailure = { e ->
                _state.update {
                    it.copy(
                        isSyncing = false,
                        syncMessage = SyncMessage.Failure(e.localizedMessage ?: "Ошибка")
                    )
                }
            },
        )
    }

    fun dismissSyncMessage() = _state.update { it.copy(syncMessage = null) }

    // ── Delete ────────────────────────────────────────────────────────────────

    fun delete(backup: BackupEntity) = viewModelScope.launch { backupDao.delete(backup) }

    // ── Restore sheet ─────────────────────────────────────────────────────────

    fun openRestoreSheet(backup: BackupEntity) = viewModelScope.launch {
        val contacts = Json.decodeFromString<List<Contact>>(backup.contactsJson).merge()
        _state.update {
            it.copy(
                restoreSheet = RestoreSheetState(
                    backup,
                    contacts.map { c -> SelectableRestoreContact(c) })
            )
        }
    }

    fun dismissRestoreSheet() = _state.update { it.copy(restoreSheet = null) }

    fun toggleRestoreContact(item: SelectableRestoreContact) = _state.update { s ->
        val sheet = s.restoreSheet ?: return@update s
        s.copy(restoreSheet = sheet.copy(contacts = sheet.contacts.map {
            if (it == item) it.copy(isSelected = !it.isSelected) else it
        }))
    }

    fun selectAllRestore() = updateRestoreContacts { it.copy(isSelected = true) }
    fun deselectAllRestore() = updateRestoreContacts { it.copy(isSelected = false) }

    fun restoreSelected() = viewModelScope.launch {
        val sheet = _state.value.restoreSheet ?: return@launch
        _state.update { it.copy(isSyncing = true) }
        val flat = sheet.contacts.filter { it.isSelected }.flatMap { rc ->
            rc.contact.phoneNumbers.map { phone -> Contact(rc.contact.name, phone) }
        }
        runCatching {
            val vcard = convertToVcard(flat)
            fileRepository.save(vcard, "restore_${sheet.backup.id}.vcf")
        }.fold(
            onSuccess = { file ->
                _state.update { it.copy(isSyncing = false, restoreSheet = null) }
                _events.send(BackupEvent.OpenVcf(file))
            },
            onFailure = { e ->
                _state.update {
                    it.copy(
                        isSyncing = false,
                        syncMessage = SyncMessage.Failure(e.localizedMessage ?: "Ошибка")
                    )
                }
            },
        )
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private fun refreshAutoBackupStatus() {
        val infos = workManager.getWorkInfosForUniqueWork(AUTO_BACKUP_TAG).get()
        _state.update {
            it.copy(isAutoBackupEnabled = infos.any { i ->
                i.state == WorkInfo.State.ENQUEUED || i.state == WorkInfo.State.RUNNING
            })
        }
    }

    private fun updateRestoreContacts(transform: (SelectableRestoreContact) -> SelectableRestoreContact) {
        _state.update { s ->
            val sheet = s.restoreSheet ?: return@update s
            s.copy(restoreSheet = sheet.copy(contacts = sheet.contacts.map(transform)))
        }
    }
}