package zelimkhan.magomadov.telegramcontacts.domain.repository

import java.io.File

interface FileRepository {
    suspend fun save(content: String, name: String): File

    suspend fun read(path: String): String
}