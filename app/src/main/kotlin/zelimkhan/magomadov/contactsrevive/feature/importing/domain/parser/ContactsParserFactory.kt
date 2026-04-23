package zelimkhan.magomadov.contactsrevive.feature.importing.domain.parser

import zelimkhan.magomadov.contactsrevive.feature.importing.domain.model.ContactsFormat

interface ContactsParserFactory {
    fun create(format: ContactsFormat): ContactsParser
}
