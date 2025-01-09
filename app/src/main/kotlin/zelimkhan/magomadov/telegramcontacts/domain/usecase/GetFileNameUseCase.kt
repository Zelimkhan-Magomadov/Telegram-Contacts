package zelimkhan.magomadov.telegramcontacts.domain.usecase

import zelimkhan.magomadov.telegramcontacts.domain.repository.FileNameRepository
import javax.inject.Inject

class GetFileNameUseCase @Inject constructor(
    private val fileNameRepository: FileNameRepository
) {
    suspend operator fun invoke(path: String): String {
        return fileNameRepository.name(path)
    }
}