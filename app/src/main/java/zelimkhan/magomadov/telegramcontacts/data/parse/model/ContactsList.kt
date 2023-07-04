package zelimkhan.magomadov.telegramcontacts.data.parse.model

import kotlinx.serialization.Serializable

@Serializable
data class ContactsList(
    val contacts: Contacts
)

val ContactsList.asDomain get() = this.contacts.list.map { it.asDomain }