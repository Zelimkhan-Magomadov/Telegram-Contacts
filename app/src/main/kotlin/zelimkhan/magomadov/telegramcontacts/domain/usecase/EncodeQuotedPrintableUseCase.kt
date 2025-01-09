package zelimkhan.magomadov.telegramcontacts.domain.usecase

import javax.inject.Inject

class EncodeQuotedPrintableUseCase @Inject constructor() {
    operator fun invoke(text: String): String = buildString {
        text.toByteArray().forEach { byte ->
            val unsignedByte = byte.toInt() and 0xFF
            if (unsignedByte !in 33..126 || byte.toInt().toChar() == '=') {
                append('=')
                append(unsignedByte.toString(16).uppercase().padStart(2, '0'))
            } else {
                append(byte.toInt().toChar())
            }
        }
    }
}