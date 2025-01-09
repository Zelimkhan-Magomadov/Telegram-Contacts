package zelimkhan.magomadov.telegramcontacts.data.pars

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import zelimkhan.magomadov.telegramcontacts.data.model.TelegramExportData
import zelimkhan.magomadov.telegramcontacts.data.model.toDomain
import zelimkhan.magomadov.telegramcontacts.domain.model.Contact
import zelimkhan.magomadov.telegramcontacts.domain.pars.ContactsParser

class TelegramContactsParser(
    private val parser: Json
) : ContactsParser {
    override suspend fun parse(json: String): List<Contact> {
        return withContext(Dispatchers.Default) {
            parser.decodeFromString<TelegramExportData>(json).contacts.list.map { it.toDomain() }
        }
    }
}