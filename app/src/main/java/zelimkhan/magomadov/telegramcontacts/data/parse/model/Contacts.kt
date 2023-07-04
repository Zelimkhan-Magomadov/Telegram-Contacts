package zelimkhan.magomadov.telegramcontacts.data.parse.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Contacts(
    @SerialName("list")
    val list: List<Contact>
)