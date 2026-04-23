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
    fun observeAll(): Flow<List<BackupEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(backup: BackupEntity)

    @Delete
    suspend fun delete(backup: BackupEntity)
}
