package zelimkhan.magomadov.telegramcontacts.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import zelimkhan.magomadov.telegramcontacts.data.parse.TelegramContactsJsonParserImpl
import zelimkhan.magomadov.telegramcontacts.data.repository.CacheFileRepository
import zelimkhan.magomadov.telegramcontacts.domain.parse.TelegramContactsJsonParser
import zelimkhan.magomadov.telegramcontacts.domain.repository.FileNameRepository
import zelimkhan.magomadov.telegramcontacts.domain.repository.FileRepository
import zelimkhan.magomadov.telegramcontacts.domain.usecase.ConvertTelegramContactsJsonToVcardUseCase
import zelimkhan.magomadov.telegramcontacts.domain.usecase.GetFileNameUseCase

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun provideFileNameRepository(@ApplicationContext context: Context): FileNameRepository {
        return CacheFileRepository(context = context)
    }

    @Provides
    fun provideFileRepository(@ApplicationContext context: Context): FileRepository {
        return CacheFileRepository(context = context)
    }

    @Provides
    fun provideContactsParser(): TelegramContactsJsonParser {
        return TelegramContactsJsonParserImpl()
    }

    @Provides
    fun provideGetFileNameUseCase(
        fileNameRepository: FileNameRepository
    ): GetFileNameUseCase {
        return GetFileNameUseCase(fileNameRepository = fileNameRepository)
    }

    @Provides
    fun provideConvertTelegramContactsJsonToVcardUseCase(
        telegramContactsJsonParser: TelegramContactsJsonParser,
        fileRepository: FileRepository
    ): ConvertTelegramContactsJsonToVcardUseCase {
        return ConvertTelegramContactsJsonToVcardUseCase(
            telegramContactsJsonParser = telegramContactsJsonParser,
            fileRepository = fileRepository
        )
    }
}