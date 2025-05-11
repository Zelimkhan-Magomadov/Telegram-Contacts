package zelimkhan.magomadov.contactsrevive.domain.contacts.model

enum class ContactsFormat {
    JSON,
    HTML;

    companion object {
        fun fromExtension(fileName: String) = when (fileName.takeLastWhile { it != '.' }) {
            "json" -> JSON
            "html" -> HTML
            else -> throw IllegalStateException()
        }
    }
}