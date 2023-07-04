package zelimkhan.magomadov.telegramcontacts.domain.parse

import zelimkhan.magomadov.telegramcontacts.domain.model.Contact

interface TelegramContactsJsonParser {
    suspend operator fun invoke(jsonString: String): List<Contact>
}
