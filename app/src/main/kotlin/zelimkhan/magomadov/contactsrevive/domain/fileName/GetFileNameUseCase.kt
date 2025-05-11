package zelimkhan.magomadov.contactsrevive.domain.fileName

import javax.inject.Inject

class GetFileNameUseCase @Inject constructor(
    private val fileNameRepository: FileNameRepository
) {
    suspend operator fun invoke(path: String): String {
        return fileNameRepository.name(path)
    }
}