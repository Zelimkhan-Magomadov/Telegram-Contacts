package zelimkhan.magomadov.contactsrevive.feature.backup.data

import android.content.Context
import android.provider.ContactsContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import zelimkhan.magomadov.contactsrevive.feature.backup.domain.repository.ContactsRepository
import zelimkhan.magomadov.contactsrevive.feature.importing.domain.model.Contact

class DeviceContactsRepository(
    private val context: Context,
) : ContactsRepository {
    private companion object {
        private val CONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        private const val DISPLAY_NAME = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
        private const val NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER
    }

    override suspend fun getDeviceContacts(): List<Contact> = withContext(Dispatchers.IO) {
        val contacts = mutableListOf<Contact>()
        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val projection = arrayOf(DISPLAY_NAME, NUMBER)
        context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            val nameCol = cursor.getColumnIndexOrThrow(DISPLAY_NAME)
            val numCol = cursor.getColumnIndexOrThrow(NUMBER)
            while (cursor.moveToNext()) {
                contacts += Contact(
                    name = cursor.getString(nameCol).orEmpty(),
                    phoneNumber = cursor.getString(numCol).orEmpty(),
                )
            }
        }
        contacts.distinctBy { it.phoneNumber }
    }
}