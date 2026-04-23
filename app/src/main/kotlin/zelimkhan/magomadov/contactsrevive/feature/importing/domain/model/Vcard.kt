package zelimkhan.magomadov.contactsrevive.feature.importing.domain.model

data class Vcard(
    val name: String,
    val phoneNumbers: List<String>,
) {
    override fun toString() = buildString {
        appendLine("BEGIN:VCARD")
        appendLine("VERSION:2.1")
        appendLine("FN;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:$name")
        phoneNumbers.forEach { appendLine("TEL:$it") }
        append("END:VCARD")
    }
}

