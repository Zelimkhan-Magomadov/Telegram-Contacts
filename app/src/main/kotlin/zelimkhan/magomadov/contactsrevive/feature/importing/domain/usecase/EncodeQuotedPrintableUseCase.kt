package zelimkhan.magomadov.contactsrevive.feature.importing.domain.usecase

class EncodeQuotedPrintableUseCase {
    operator fun invoke(text: String): String = buildString {
        text.toByteArray(Charsets.UTF_8).forEach { byte ->
            val unsigned = byte.toInt() and 0xFF
            if (unsigned in 33..126 && byte.toInt().toChar() != '=') {
                append(byte.toInt().toChar())
            } else {
                append('=')
                append(unsigned.toString(16).uppercase().padStart(2, '0'))
            }
        }
    }
}