package zelimkhan.magomadov.contactsrevive.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import zelimkhan.magomadov.contactsrevive.domain.contacts.model.Contact

@Serializable
data class ContactData(
    @SerialName("first_name")
    val firstName: String,
    @SerialName("last_name")
    val lastName: String,
    @SerialName("phone_number")
    val phoneNumber: String
)

fun ContactData.toDomain(): Contact {
    return Contact(
        name = "${this.firstName} ${this.lastName}",
        phoneNumber = this.phoneNumber
    )
}