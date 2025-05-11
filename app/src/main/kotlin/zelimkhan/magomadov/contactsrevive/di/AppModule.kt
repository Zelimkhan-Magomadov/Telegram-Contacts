package zelimkhan.magomadov.contactsrevive.di

import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import zelimkhan.magomadov.contactsrevive.data.parser.HtmlContactsParser
import zelimkhan.magomadov.contactsrevive.data.parser.JsonContactsParser
import zelimkhan.magomadov.contactsrevive.data.repository.AndroidFileNameRepository
import zelimkhan.magomadov.contactsrevive.data.repository.CacheFileRepository
import zelimkhan.magomadov.contactsrevive.domain.contacts.model.ContactsFormat
import zelimkhan.magomadov.contactsrevive.domain.contacts.ContactsParser
import zelimkhan.magomadov.contactsrevive.domain.fileName.FileNameRepository
import zelimkhan.magomadov.contactsrevive.domain.contacts.FileRepository

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