package zelimkhan.magomadov.telegramcontacts.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import zelimkhan.magomadov.telegramcontacts.domain.repository.FileNameRepository

class GetFileNameUseCase(
    private val fileNameRepository: FileNameRepository
) {
    suspend operator fun invoke(path: String): String {
        return withContext(Dispatchers.IO) {
            fileNameRepository.getFileName(path)
        }
    }
}