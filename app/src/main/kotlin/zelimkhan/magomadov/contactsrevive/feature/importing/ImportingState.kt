package zelimkhan.magomadov.contactsrevive.feature.importing

import zelimkhan.magomadov.contactsrevive.feature.importing.domain.model.Contact
import java.io.File

data class ImportingState(
    val selectedFileName: String = "",
    val isFileSelected: Boolean = false,
    val isFileConverted: Boolean = false,
    val convertedFile: File? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val contacts: List<SelectableContact> = emptyList(),
    val showContactsList: Boolean = false,
    val importResult: ImportResult? = null,
)

data class ImportResult(
    val totalSelected: Int,
    val successCount: Int,
    val fileName: String,
)

data class SelectableContact(
    val contact: Contact,
    val isSelected: Boolean = true,
)
