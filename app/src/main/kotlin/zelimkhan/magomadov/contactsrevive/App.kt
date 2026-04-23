package zelimkhan.magomadov.contactsrevive

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin
import zelimkhan.magomadov.contactsrevive.core.di.coreModule
import zelimkhan.magomadov.contactsrevive.feature.backup.di.backupModule
import zelimkhan.magomadov.contactsrevive.feature.importing.di.importingModule
import zelimkhan.magomadov.contactsrevive.feature.instruction.di.instructionModule

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            workManagerFactory()
            modules(
                coreModule,
                importingModule,
                backupModule,
                instructionModule
            )
        }
    }
}
