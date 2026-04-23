package zelimkhan.magomadov.contactsrevive.feature.importing.domain.usecase

class FormatPhoneNumberUseCase {
    operator fun invoke(phone: String): String {
        return if (phone.startsWith('0')) "+${phone.trimStart('0')}" else phone
    }
}