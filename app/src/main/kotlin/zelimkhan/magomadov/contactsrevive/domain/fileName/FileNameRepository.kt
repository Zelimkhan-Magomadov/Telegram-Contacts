package zelimkhan.magomadov.contactsrevive.domain.fileName

interface FileNameRepository {
    suspend fun name(path: String): String
}