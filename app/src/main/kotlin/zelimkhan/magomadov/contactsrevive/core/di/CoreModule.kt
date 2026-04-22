package zelimkhan.magomadov.contactsrevive.core.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import zelimkhan.magomadov.contactsrevive.core.database.AppDatabase

val coreModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "contacts_revive_db"
        ).build()
    }

    single { get<AppDatabase>().backupDao() }
}
