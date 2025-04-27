package zelimkhan.magomadov.telegramcontacts.di

import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import zelimkhan.magomadov.telegramcontacts.data.parser.HtmlContactsParser
import zelimkhan.magomadov.telegramcontacts.data.parser.JsonContactsParser
import zelimkhan.magomadov.telegramcontacts.data.repository.AndroidFileNameRepository
import zelimkhan.magomadov.telegramcontacts.data.repository.CacheFileRepository
import zelimkhan.magomadov.telegramcontacts.domain.model.ContactsFormat
import zelimkhan.magomadov.telegramcontacts.domain.parser.ContactsParser
import zelimkhan.magomadov.telegramcontacts.domain.repository.FileNameRepository
import zelimkhan.magomadov.telegramcontacts.domain.repository.FileRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    abstract fun bindFileRepository(cacheFileRepository: CacheFileRepository): FileRepository

    @Binds
    abstract fun bindFileNameRepository(androidFileNameRepository: AndroidFileNameRepository): FileNameRepository

    @Binds
    @IntoMap
    @ContactsParserKey(ContactsFormat.HTML)
    abstract fun bindHtmlContactsParser(parser: HtmlContactsParser): ContactsParser

    @Binds
    @IntoMap
    @ContactsParserKey(ContactsFormat.JSON)
    abstract fun bindJsonContactsParser(parser: JsonContactsParser): ContactsParser
}

@MapKey
annotation class ContactsParserKey(val value: ContactsFormat)