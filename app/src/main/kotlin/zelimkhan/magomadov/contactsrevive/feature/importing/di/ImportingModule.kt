package zelimkhan.magomadov.contactsrevive.feature.importing.di

import androidx.work.WorkManager
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import zelimkhan.magomadov.contactsrevive.feature.backup.data.AutoBackupWorker
import zelimkhan.magomadov.contactsrevive.feature.importing.data.parser.ContactsParserFactoryImpl
import zelimkhan.magomadov.contactsrevive.feature.importing.data.parser.HtmlContactsParser
import zelimkhan.magomadov.contactsrevive.feature.importing.data.parser.JsonContactsParser
import zelimkhan.magomadov.contactsrevive.feature.importing.data.repository.CacheFileRepository
import zelimkhan.magomadov.contactsrevive.feature.importing.domain.parser.ContactsParserFactory
import zelimkhan.magomadov.contactsrevive.feature.importing.domain.repository.FileRepository
import zelimkhan.magomadov.contactsrevive.feature.importing.domain.usecase.ConvertContactsToVcardUseCase
import zelimkhan.magomadov.contactsrevive.feature.importing.domain.usecase.EncodeQuotedPrintableUseCase
import zelimkhan.magomadov.contactsrevive.feature.importing.domain.usecase.FormatPhoneNumberUseCase
import zelimkhan.magomadov.contactsrevive.feature.importing.ui.importing.ImportingViewModel

val importingModule = module {
    singleOf(::CacheFileRepository) { bind<FileRepository>() }

    factoryOf(::HtmlContactsParser)
    factoryOf(::JsonContactsParser)
    factoryOf(::ContactsParserFactoryImpl) { bind<ContactsParserFactory>() }

    factoryOf(::EncodeQuotedPrintableUseCase)
    factoryOf(::FormatPhoneNumberUseCase)
    factoryOf(::ConvertContactsToVcardUseCase)

    single { WorkManager.getInstance(androidContext()) }

    viewModelOf(::ImportingViewModel)

    workerOf(::AutoBackupWorker)
}
