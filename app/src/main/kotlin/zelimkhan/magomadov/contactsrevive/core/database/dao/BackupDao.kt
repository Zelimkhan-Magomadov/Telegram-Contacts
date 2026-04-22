package zelimkhan.magomadov.contactsrevive.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import zelimkhan.magomadov.contactsrevive.core.database.entity.BackupEntity

@Dao
interface BackupDao {
    @Query("SELECT * FROM backups ORDER BY date DESC")
    fun getAllBackups(): Flow<List<BackupEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBackup(backup: BackupEntity)

    @Delete
    suspend fun deleteBackup(backup: BackupEntity)

    @Query("SELECT * FROM backups WHERE id = :id")
    suspend fun getBackupById(id: Long): BackupEntity?
}
