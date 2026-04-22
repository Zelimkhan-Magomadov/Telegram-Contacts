package zelimkhan.magomadov.contactsrevive.feature.backup

import zelimkhan.magomadov.contactsrevive.core.database.entity.BackupEntity

data class BackupState(
    val backups: List<BackupEntity> = emptyList(),
    val isLoading: Boolean = false,
    val isAutoBackupEnabled: Boolean = false,
)
