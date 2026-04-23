package zelimkhan.magomadov.contactsrevive.feature.importing.data.parser

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import zelimkhan.magomadov.contactsrevive.feature.importing.data.model.TelegramExportDto
import zelimkhan.magomadov.contactsrevive.feature.importing.data.model.toDomain
import zelimkhan.magomadov.contactsrevive.feature.importing.domain.model.Contact
import zelimkhan.magomadov.contactsrevive.feature.importing.domain.parser.ContactsParser

class JsonContactsParser : ContactsParser {
    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun invoke(content: String): List<Contact> = withContext(Dispatchers.Default) {
        json.decodeFromString<TelegramExportDto>(content).contacts.list.map { it.toDomain() }
    }
}