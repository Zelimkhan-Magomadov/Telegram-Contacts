package zelimkhan.magomadov.telegramcontacts.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import zelimkhan.magomadov.telegramcontacts.domain.model.Contact
import zelimkhan.magomadov.telegramcontacts.domain.model.Vcard
import zelimkhan.magomadov.telegramcontacts.domain.repository.FileRepository
import zelimkhan.magomadov.telegramcontacts.domain.parse.TelegramContactsJsonParser
import java.io.File

class ConvertTelegramContactsJsonToVcardUseCase(
    private val telegramContactsJsonParser: TelegramContactsJsonParser,
    private val fileRepository: FileRepository
) {
    suspend operator fun invoke(jsonFilePath: String, convertedFileName: String): File {
        return withContext(Dispatchers.Default) {
            val json = fileRepository.getContentFromFile(jsonFilePath)
            val contacts = telegramContactsJsonParser(json)
            val vcardList = convertContactsToVcardList(contacts)
            createVcardFile(vcardList, convertedFileName)
        }
    }

    private fun convertContactsToVcardList(contacts: List<Contact>): List<Vcard> {
        return contacts.groupBy(
            keySelector = { getContactName(it) },
            valueTransform = { formatPhoneNumber(it.phoneNumber) }
        ).map { group -> Vcard(name = group.key, phoneNumbers = group.value) }
    }

    private fun getContactName(contact: Contact): String {
        return if (contact.firstName.isNotEmpty() && contact.lastName.isNotEmpty())
            "${contact.firstName} ${contact.lastName}"
        else if (contact.firstName.isNotEmpty())
            contact.firstName
        else
            contact.lastName
    }

    private fun formatPhoneNumber(phoneNumber: String): String {
        return if (phoneNumber.startsWith('0'))
            "+${phoneNumber.trimStart('0')}"
        else
            phoneNumber
    }

    private suspend fun createVcardFile(vcardList: List<Vcard>, fileName: String): File {
        val vcardText = formatVcardList(vcardList)
        return fileRepository.createFile(content = vcardText, fileName = fileName)
    }

    private fun formatVcardList(vcardList: List<Vcard>): String {
        return vcardList.joinToString("") { it.toString() + "\n" }
    }
}

