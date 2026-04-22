package zelimkhan.magomadov.contactsrevive.feature.importing.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FormatPhoneNumberUseCase {
    suspend operator fun invoke(phoneNumber: String): String {
        return withContext(Dispatchers.Default) {
            if (phoneNumber.startsWith('0')) {
                "+${phoneNumber.trimStart('0')}"
            } else {
                phoneNumber
            }
        }
    }
}