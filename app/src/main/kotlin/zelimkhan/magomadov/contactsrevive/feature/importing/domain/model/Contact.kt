package zelimkhan.magomadov.contactsrevive.feature.importing.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Contact(
    val name: String,
    val phoneNumber: String,
)

fun List<Contact>.merge(): List<MergedContact> {
    return groupBy { it.name.trim() }.map { (name, list) ->
        MergedContact(name, list.map { it.phoneNumber }.distinct())
    }
}
