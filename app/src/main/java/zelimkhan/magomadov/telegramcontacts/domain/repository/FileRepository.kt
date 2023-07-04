package zelimkhan.magomadov.telegramcontacts.domain.repository

import java.io.File

interface FileRepository {
    suspend fun createFile(content: String, fileName: String): File
    suspend fun getContentFromFile(path: String): String
}