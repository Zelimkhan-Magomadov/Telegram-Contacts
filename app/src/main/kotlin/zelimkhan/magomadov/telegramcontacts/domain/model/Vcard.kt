package zelimkhan.magomadov.telegramcontacts.domain.model

data class Vcard(
    val name: String,
    val phoneNumbers: List<String>
) {
    override fun toString(): String {
        return buildString {
            appendLine("BEGIN:VCARD")
            appendLine("VERSION:2.1")
            appendLine("FN;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:$name")
            phoneNumbers.forEach { appendLine("TEL:$it") }
            append("END:VCARD")
        }
    }
}

