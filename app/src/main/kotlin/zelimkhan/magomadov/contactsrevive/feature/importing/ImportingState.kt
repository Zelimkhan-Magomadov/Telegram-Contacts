package zelimkhan.magomadov.contactsrevive.feature.importing

import java.io.File

data class ImportingState(
    val selectedFileName: String = "",
    val isFileSelected: Boolean = false,
    val isFileConverted: Boolean = false,
    val convertedFile: File? = null,
)