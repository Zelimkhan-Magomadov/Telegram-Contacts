package zelimkhan.magomadov.telegramcontacts.main

sealed class MainViewIntent {
    data object Convert : MainViewIntent()

    data class FileSelected(val filePath: String) : MainViewIntent()
}