package zelimkhan.magomadov.contactsrevive.feature.importing.domain.parser

import zelimkhan.magomadov.contactsrevive.feature.importing.domain.model.Contact

interface ContactsParser {
    suspend operator fun invoke(content: String): List<Contact>
}