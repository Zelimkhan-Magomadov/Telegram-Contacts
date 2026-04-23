package zelimkhan.magomadov.contactsrevive.feature.importing.domain.model

data class MergedContact(
    val name: String,
    val phoneNumbers: List<String>,
) {
    val primaryPhone: String get() = phoneNumbers.first()
    val hasMultiplePhones: Boolean get() = phoneNumbers.size > 1
}