package zelimkhan.magomadov.contactsrevive.feature.importing.data.model

import kotlinx.serialization.Serializable

@Serializable
data class TelegramExportData(
    val contacts: Contacts
)