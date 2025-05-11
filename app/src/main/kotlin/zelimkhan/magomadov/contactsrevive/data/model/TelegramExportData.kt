package zelimkhan.magomadov.contactsrevive.data.model

import kotlinx.serialization.Serializable

@Serializable
data class TelegramExportData(
    val contacts: Contacts
)