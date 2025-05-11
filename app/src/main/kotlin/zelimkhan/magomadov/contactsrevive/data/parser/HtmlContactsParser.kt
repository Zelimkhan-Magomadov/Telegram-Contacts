package zelimkhan.magomadov.contactsrevive.data.parser

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import zelimkhan.magomadov.contactsrevive.domain.contacts.model.Contact
import zelimkhan.magomadov.contactsrevive.domain.contacts.ContactsParser
import javax.inject.Inject

class HtmlContactsParser @Inject constructor() : ContactsParser {
    override suspend fun invoke(content: String): List<Contact> {
        return withContext(Dispatchers.Default) {
            val document = Jsoup.parse(content)
            val entries = document.select("div.entry")
            entries.map {
                val name = it.selectFirst("div.name")?.text()?.trim().orEmpty()
                val phone = it.selectFirst("div.details_entry")?.text()?.trim().orEmpty()
                Contact(name = name, phoneNumber = phone)
            }
        }
    }
}