package zelimkhan.magomadov.telegramcontacts.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Contacts(
    val list: List<ContactData>
)