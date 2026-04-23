package zelimkhan.magomadov.contactsrevive.feature.importing.ui.importing

import zelimkhan.magomadov.contactsrevive.feature.importing.domain.model.MergedContact
import java.io.File

data class ImportingState(
    val step: ImportStep = ImportStep.FilePicker,
    val fileName: String = "",
    val totalRaw: Int = 0,
    val mergeGroups: List<MergeGroup> = emptyList(),
    val duplicateGroups: List<DuplicateGroup> = emptyList(),
    val mergeByName: Boolean = true,
    val removeDuplicates: Boolean = true,
    val contacts: List<SelectableContact> = emptyList(),
    val resultFile: File? = null,
    val summary: ImportSummary? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

data class SelectableContact(
    val contact: MergedContact,
    val isSelected: Boolean = true,
)

data class MergeGroup(
    val name: String,
    val phones: List<String>,
)

data class DuplicateGroup(
    val phone: String,
    val names: List<String>,
)

data class ImportSummary(
    val imported: Int,
    val mergedGroups: Int,
    val removedDuplicates: Int,
)

enum class ImportStep {
    FilePicker, Options, Preview, ContactsList, Result
}