package zelimkhan.magomadov.contactsrevive.feature.importing.data.parser

import zelimkhan.magomadov.contactsrevive.feature.importing.domain.model.Contact
import zelimkhan.magomadov.contactsrevive.feature.importing.domain.parser.ContactsParser

class ContactsParserImpl(
    private val jsonContactsParser: JsonContactsParser,
    private val htmlContactsParser: HtmlContactsParser,
) : ContactsParser {
    override suspend fun invoke(content: String): List<Contact> {
        return emptyList()
    }
}