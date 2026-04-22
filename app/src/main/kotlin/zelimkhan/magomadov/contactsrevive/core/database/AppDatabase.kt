package zelimkhan.magomadov.contactsrevive.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import zelimkhan.magomadov.contactsrevive.core.database.dao.BackupDao
import zelimkhan.magomadov.contactsrevive.core.database.entity.BackupEntity

@Database(entities = [BackupEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun backupDao(): BackupDao
}
