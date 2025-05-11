package zelimkhan.magomadov.contactsrevive.domain.contacts

import zelimkhan.magomadov.contactsrevive.domain.contacts.model.Contact

interface ContactsParser {
    suspend operator fun invoke(content: String): List<Contact>
}