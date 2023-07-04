package zelimkhan.magomadov.telegramcontacts.data.parse.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Contact(
    @SerialName("first_name")
    val firstName: String,
    @SerialName("last_name")
    val lastName: String,
    @SerialName("phone_number")
    val phoneNumber: String
)

val Contact.asDomain
    get() = zelimkhan.magomadov.telegramcontacts.domain.model.Contact(
        firstName = firstName,
        lastName = lastName,
        phoneNumber = phoneNumber
    )