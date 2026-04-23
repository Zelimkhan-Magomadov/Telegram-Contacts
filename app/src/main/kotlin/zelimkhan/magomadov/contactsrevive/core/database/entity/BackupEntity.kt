package zelimkhan.magomadov.contactsrevive.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "backups")
data class BackupEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val date: Long,
    val contactCount: Int,
    val contactsJson: String,
)
