package zelimkhan.magomadov.telegramcontacts.main

import java.io.File

data class MainState(
    val selectedFileName: String = "",
    val isFileSelected: Boolean = false,
    val isFileConverted: Boolean = false,
    val convertedFile: File? = null
)