package zelimkhan.magomadov.telegramcontacts.data.model

import kotlinx.serialization.Serializable

@Serializable
data class TelegramExportData(
    val contacts: Contacts
)