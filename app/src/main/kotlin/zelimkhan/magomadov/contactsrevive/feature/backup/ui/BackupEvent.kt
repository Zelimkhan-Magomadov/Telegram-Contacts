package zelimkhan.magomadov.contactsrevive.feature.backup.ui

import java.io.File

sealed interface BackupEvent {
    data class OpenVcf(val file: File) : BackupEvent
}