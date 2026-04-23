package zelimkhan.magomadov.contactsrevive.feature.importing.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import zelimkhan.magomadov.contactsrevive.feature.importing.domain.model.Contact

@Serializable
data class ContactDto(
    @SerialName("first_name") val firstName: String,
    @SerialName("last_name") val lastName: String,
    @SerialName("phone_number") val phoneNumber: String,
)

fun ContactDto.toDomain() = Contact(
    name = "$firstName $lastName".trim(),
    phoneNumber = phoneNumber,
)