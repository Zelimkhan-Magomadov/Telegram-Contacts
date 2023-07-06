package zelimkhan.magomadov.telegramcontacts.presentation.convert

import java.io.File

sealed interface ConvertViewEvent {
    object OpenFilePicker : ConvertViewEvent
    class OpenFile(val file: File) : ConvertViewEvent
    class SendFile(val file: File) : ConvertViewEvent
}