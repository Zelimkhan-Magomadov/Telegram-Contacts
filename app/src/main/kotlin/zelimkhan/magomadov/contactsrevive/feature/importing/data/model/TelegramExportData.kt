package zelimkhan.magomadov.contactsrevive.feature.importing.data.model

import kotlinx.serialization.Serializable

@Serializable
data class TelegramExportDto(
    val contacts: ContactsDto,
)