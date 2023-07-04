package zelimkhan.magomadov.telegramcontacts.data.parse

import zelimkhan.magomadov.telegramcontacts.data.parse.model.ContactsList
import zelimkhan.magomadov.telegramcontacts.data.parse.model.asDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import zelimkhan.magomadov.telegramcontacts.domain.model.Contact
import zelimkhan.magomadov.telegramcontacts.domain.parse.TelegramContactsJsonParser

class TelegramContactsJsonParserImpl : TelegramContactsJsonParser {
    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun invoke(jsonString: String): List<Contact> {
        return withContext(Dispatchers.Default) {
            json.decodeFromString<ContactsList>(jsonString).asDomain
        }
    }
}