package zelimkhan.magomadov.contactsrevive.domain.contacts

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import zelimkhan.magomadov.contactsrevive.domain.contacts.model.Contact
import zelimkhan.magomadov.contactsrevive.domain.contacts.model.ContactsFormat
import zelimkhan.magomadov.contactsrevive.domain.contacts.model.Vcard
import javax.inject.Inject

typealias VcardFilePath = String

class ConvertContactsToVcardUseCase @Inject constructor(
    private val fileRepository: FileRepository,
    private val parsers: Map<ContactsFormat, @JvmSuppressWildcards ContactsParser>,
    private val encodeQuotedPrintableUseCase: EncodeQuotedPrintableUseCase,
    private val formatPhoneNumberUseCase: FormatPhoneNumberUseCase,
) {
    suspend operator fun invoke(filePath: String, format: ContactsFormat): VcardFilePath {
        return withContext(Dispatchers.Default) {
            val parser = parsers[format] ?: error("Parser not found for format: $format")
            val content = fileRepository.read(filePath)
            val contacts = parser(content)
            val vCardContacts = convertToVcard(contacts)
            vCardContacts.joinToString("\r\n")
        }
    }

    private suspend fun convertToVcard(contacts: List<Contact>): List<Vcard> {
        return contacts.groupBy(
            keySelector = { it.name },
            valueTransform = { formatPhoneNumberUseCase(it.phoneNumber) }
        ).map { (name, phoneNumbers) ->
            Vcard(name = encodeQuotedPrintableUseCase(name), phoneNumbers = phoneNumbers)
        }
    }
}