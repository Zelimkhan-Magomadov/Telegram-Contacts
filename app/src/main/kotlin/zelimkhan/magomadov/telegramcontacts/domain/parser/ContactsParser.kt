package zelimkhan.magomadov.telegramcontacts.domain.parser

import zelimkhan.magomadov.telegramcontacts.domain.model.Contact

interface ContactsParser {
    suspend operator fun invoke(content: String): List<Contact>
}