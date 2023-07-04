package zelimkhan.magomadov.telegramcontacts.domain.repository

interface FileNameRepository {
    suspend fun getFileName(path: String): String
}