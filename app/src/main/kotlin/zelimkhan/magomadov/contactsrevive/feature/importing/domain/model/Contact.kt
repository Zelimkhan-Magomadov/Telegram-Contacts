package zelimkhan.magomadov.contactsrevive.feature.importing.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Contact(
    val name: String,
    val phoneNumber: String,
)
