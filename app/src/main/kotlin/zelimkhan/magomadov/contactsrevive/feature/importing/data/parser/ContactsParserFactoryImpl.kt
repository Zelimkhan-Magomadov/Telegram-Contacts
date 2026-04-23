package zelimkhan.magomadov.contactsrevive.feature.importing.data.parser

import zelimkhan.magomadov.contactsrevive.feature.importing.domain.model.ContactsFormat
import zelimkhan.magomadov.contactsrevive.feature.importing.domain.parser.ContactsParser
import zelimkhan.magomadov.contactsrevive.feature.importing.domain.parser.ContactsParserFactory

class ContactsParserFactoryImpl(
    private val jsonParser: JsonContactsParser,
    private val htmlParser: HtmlContactsParser,
) : ContactsParserFactory {
    override fun create(format: ContactsFormat): ContactsParser {
        return when (format) {
            ContactsFormat.JSON -> jsonParser
            ContactsFormat.HTML -> htmlParser
        }
    }
}
