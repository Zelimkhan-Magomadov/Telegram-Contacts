package zelimkhan.magomadov.telegramcontacts.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import zelimkhan.magomadov.telegramcontacts.domain.model.Contact
import zelimkhan.magomadov.telegramcontacts.domain.model.Vcard
import zelimkhan.magomadov.telegramcontacts.domain.pars.ContactsParser
import zelimkhan.magomadov.telegramcontacts.domain.repository.FileRepository
import javax.inject.Inject

typealias VcardFilePath = String

class ConvertJsonToVcardUseCase @Inject constructor(
    private val fileRepository: FileRepository,
    private val contactsParser: ContactsParser,
    private val encodeQuotedPrintableUseCase: EncodeQuotedPrintableUseCase,
    private val formatPhoneNumberUseCase: FormatPhoneNumberUseCase,
) {
    suspend operator fun invoke(jsonFilePath: String): VcardFilePath {
        return withContext(Dispatchers.Default) {
            val json = fileRepository.read(jsonFilePath)
            val contacts = contactsParser.parse(json)
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