package zelimkhan.magomadov.contactsrevive.feature.importing.di

import androidx.work.WorkManager
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import zelimkhan.magomadov.contactsrevive.feature.backup.AutoBackupWorker
import zelimkhan.magomadov.contactsrevive.feature.backup.BackupViewModel
import zelimkhan.magomadov.contactsrevive.feature.importing.ImportingViewModel
import zelimkhan.magomadov.contactsrevive.feature.importing.data.parser.HtmlContactsParser
import zelimkhan.magomadov.contactsrevive.feature.importing.data.parser.JsonContactsParser
import zelimkhan.magomadov.contactsrevive.feature.importing.data.repository.CacheFileRepository
import zelimkhan.magomadov.contactsrevive.feature.importing.domain.model.ContactsFormat
import zelimkhan.magomadov.contactsrevive.feature.importing.domain.parser.ContactsParser
import zelimkhan.magomadov.contactsrevive.feature.importing.domain.repository.FileRepository
import zelimkhan.magomadov.contactsrevive.feature.importing.domain.usecase.ConvertContactsToVcardUseCase
import zelimkhan.magomadov.contactsrevive.feature.importing.domain.usecase.EncodeQuotedPrintableUseCase
import zelimkhan.magomadov.contactsrevive.feature.importing.domain.usecase.FormatPhoneNumberUseCase
import zelimkhan.magomadov.contactsrevive.feature.instruction.InstructionViewModel

val importingModule = module {
    single<FileRepository> { CacheFileRepository(get()) }

    factory<ContactsParser>(named(ContactsFormat.HTML.name)) { HtmlContactsParser() }
    factory<ContactsParser>(named(ContactsFormat.JSON.name)) { JsonContactsParser() }

    factoryOf(::EncodeQuotedPrintableUseCase)
    factoryOf(::FormatPhoneNumberUseCase)
    factoryOf(::ConvertContactsToVcardUseCase)

    single { WorkManager.getInstance(androidContext()) }

    viewModel { ImportingViewModel(get(), get(), get()) }
    viewModel { BackupViewModel(get(), get(), androidContext(), get()) }
    viewModelOf(::InstructionViewModel)

    workerOf(::AutoBackupWorker)
}
