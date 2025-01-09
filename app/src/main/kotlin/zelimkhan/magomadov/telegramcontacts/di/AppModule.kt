package zelimkhan.magomadov.telegramcontacts.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import zelimkhan.magomadov.telegramcontacts.data.repository.AndroidFileNameRepository
import zelimkhan.magomadov.telegramcontacts.data.repository.CacheFileRepository
import zelimkhan.magomadov.telegramcontacts.data.pars.TelegramContactsParser
import zelimkhan.magomadov.telegramcontacts.domain.pars.ContactsParser
import zelimkhan.magomadov.telegramcontacts.domain.repository.FileNameRepository
import zelimkhan.magomadov.telegramcontacts.domain.repository.FileRepository

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideFileRepository(@ApplicationContext context: Context): FileRepository {
        return CacheFileRepository(context)
    }

    @Provides
    fun provideFileNameRepository(@ApplicationContext context: Context): FileNameRepository {
        return AndroidFileNameRepository(context)
    }

    @Provides
    fun provideContactsParser(parser: Json): ContactsParser {
        return TelegramContactsParser(parser)
    }

    @Provides
    fun provideParser(): Json {
        return Json { ignoreUnknownKeys = true }
    }
}