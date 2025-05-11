package zelimkhan.magomadov.contactsrevive.ui.conversion

import java.io.File

data class ConversionState(
    val selectedFileName: String = "",
    val isFileSelected: Boolean = false,
    val isFileConverted: Boolean = false,
    val convertedFile: File? = null
)