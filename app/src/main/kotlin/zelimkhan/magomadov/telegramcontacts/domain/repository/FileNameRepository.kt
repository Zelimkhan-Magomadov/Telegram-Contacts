package zelimkhan.magomadov.telegramcontacts.domain.repository

interface FileNameRepository {
    suspend fun name(path: String): String
}