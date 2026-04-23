package zelimkhan.magomadov.contactsrevive.feature.backup.ui

import zelimkhan.magomadov.contactsrevive.core.database.entity.BackupEntity
import zelimkhan.magomadov.contactsrevive.feature.importing.domain.model.MergedContact

data class BackupState(
    val backups: List<BackupEntity> = emptyList(),
    val isAutoBackupEnabled: Boolean = false,
    val isSyncing: Boolean = false,
    val syncMessage: SyncMessage? = null,
    val restoreSheet: RestoreSheetState? = null,
)

data class RestoreSheetState(
    val backup: BackupEntity,
    val contacts: List<SelectableRestoreContact>,
) {
    val selectedCount: Int get() = contacts.count { it.isSelected }
}

data class SelectableRestoreContact(
    val contact: MergedContact,
    val isSelected: Boolean = true,
)

sealed interface SyncMessage {
    data object Success : SyncMessage
    data class Failure(val message: String) : SyncMessage
}
