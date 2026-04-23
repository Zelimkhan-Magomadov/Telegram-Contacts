package zelimkhan.magomadov.contactsrevive.feature.importing.domain.usecase

import zelimkhan.magomadov.contactsrevive.feature.importing.domain.model.Contact
import zelimkhan.magomadov.contactsrevive.feature.importing.domain.model.Vcard

class ConvertContactsToVcardUseCase(
    private val encodeQP: EncodeQuotedPrintableUseCase,
    private val formatPhone: FormatPhoneNumberUseCase,
) {
    operator fun invoke(contacts: List<Contact>): String {
        return contacts.groupBy { it.name.trim() }.map { (name, list) ->
            Vcard(
                name = encodeQP(name),
                phoneNumbers = list.map { formatPhone(it.phoneNumber) }.distinct(),
            )
        }.joinToString("\r\n")
    }
}
