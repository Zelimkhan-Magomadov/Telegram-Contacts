package zelimkhan.magomadov.telegramcontacts.domain.model

data class Vcard(
    val name: String,
    val phoneNumbers: List<String>
) {
    override fun toString(): String {
        return """
            |BEGIN:VCARD
            |VERSION:2.1
            |FN;ENCODING=QUOTED-PRINTABLE:$name
            |${phoneNumbers.joinToString("") { "TEL;CELL:$it\n" }.dropLast(1)}
            |END:VCARD
            |
            """.trimMargin()
    }
}
