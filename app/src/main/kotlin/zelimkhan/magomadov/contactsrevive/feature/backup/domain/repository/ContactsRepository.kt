package zelimkhan.magomadov.contactsrevive.feature.backup.domain.repository

import zelimkhan.magomadov.contactsrevive.feature.importing.domain.model.Contact

interface ContactsRepository {
    suspend fun getDeviceContacts(): List<Contact>
}