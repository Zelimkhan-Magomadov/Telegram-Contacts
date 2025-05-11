package zelimkhan.magomadov.contactsrevive.data.parser

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import zelimkhan.magomadov.contactsrevive.data.model.TelegramExportData
import zelimkhan.magomadov.contactsrevive.data.model.toDomain
import zelimkhan.magomadov.contactsrevive.domain.contacts.model.Contact
import zelimkhan.magomadov.contactsrevive.domain.contacts.ContactsParser
import javax.inject.Inject

class JsonContactsParser @Inject constructor() : ContactsParser {
    private val parser = Json { ignoreUnknownKeys = true }

    override suspend fun invoke(content: String): List<Contact> {
        return withContext(Dispatchers.Default) {
            parser.decodeFromString<TelegramExportData>(content).contacts.list.map { it.toDomain() }
        }
    }
}