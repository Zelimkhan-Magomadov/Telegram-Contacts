package zelimkhan.magomadov.contactsrevive.feature.importing.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import zelimkhan.magomadov.contactsrevive.feature.importing.domain.model.Contact
import zelimkhan.magomadov.contactsrevive.feature.importing.domain.model.Vcard
import zelimkhan.magomadov.contactsrevive.feature.importing.domain.parser.ContactsParser
import zelimkhan.magomadov.contactsrevive.feature.importing.domain.repository.FileRepository

typealias VcardFilePath = String

class ConvertContactsToVcardUseCase(
    private val fileRepository: FileRepository,
    private val encodeQuotedPrintableUseCase: EncodeQuotedPrintableUseCase,
    private val formatPhoneNumberUseCase: FormatPhoneNumberUseCase,
) {
    suspend operator fun invoke(filePath: String, parser: ContactsParser): VcardFilePath {
        return withContext(Dispatchers.Default) {
            val content = fileRepository.read(filePath)
            val contacts = parser(content)
            invoke(contacts)
        }
    }

    suspend operator fun invoke(contacts: List<Contact>): VcardFilePath {
        return withContext(Dispatchers.Default) {
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
