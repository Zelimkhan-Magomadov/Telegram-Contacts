package zelimkhan.magomadov.contactsrevive.feature.backup

import android.content.Context
import android.provider.ContactsContract
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import zelimkhan.magomadov.contactsrevive.core.database.dao.BackupDao
import zelimkhan.magomadov.contactsrevive.core.database.entity.BackupEntity
import zelimkhan.magomadov.contactsrevive.feature.importing.domain.model.Contact

class AutoBackupWorker(
    appContext: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(appContext, workerParams), KoinComponent {

    private val backupDao: BackupDao by inject()

    override suspend fun doWork(): Result {
        return try {
            val localContacts = getLocalContacts()
            if (localContacts.isNotEmpty()) {
                backupDao.insertBackup(
                    BackupEntity(
                        name = "Auto Backup ${System.currentTimeMillis()}",
                        date = System.currentTimeMillis(),
                        contactCount = localContacts.size,
                        contactsJson = Json.encodeToString(localContacts)
                    )
                )
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun getLocalContacts(): List<Contact> {
        val contacts = mutableListOf<Contact>()
        val cursor = applicationContext.contentResolver.query(
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
