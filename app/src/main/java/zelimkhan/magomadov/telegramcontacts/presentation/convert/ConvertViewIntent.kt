package zelimkhan.magomadov.telegramcontacts.presentation.convert

sealed interface ConvertViewIntent {
    object SelectFile : ConvertViewIntent
    object ConvertFile : ConvertViewIntent
    object OpenFile : ConvertViewIntent
    object FileOpened : ConvertViewIntent
    object SendFile: ConvertViewIntent
    object FileSent : ConvertViewIntent
    class FileSelected(val filePath: String) : ConvertViewIntent
}