package zelimkhan.magomadov.contactsrevive.domain.contacts

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FormatPhoneNumberUseCase @Inject constructor() {
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