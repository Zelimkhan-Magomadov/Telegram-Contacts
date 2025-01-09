package zelimkhan.magomadov.telegramcontacts.domain.pars

import zelimkhan.magomadov.telegramcontacts.domain.model.Contact

interface ContactsParser {
    suspend fun parse(json: String): List<Contact>
}