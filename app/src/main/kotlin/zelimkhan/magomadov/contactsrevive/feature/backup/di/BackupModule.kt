package zelimkhan.magomadov.contactsrevive.feature.backup.di

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import zelimkhan.magomadov.contactsrevive.feature.backup.data.DeviceContactsRepository
import zelimkhan.magomadov.contactsrevive.feature.backup.domain.repository.ContactsRepository
import zelimkhan.magomadov.contactsrevive.feature.backup.ui.BackupViewModel

val backupModule = module {
    singleOf(::DeviceContactsRepository) { bind<ContactsRepository>() }
    viewModelOf(::BackupViewModel)
}