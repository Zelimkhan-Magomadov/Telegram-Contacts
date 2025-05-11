package zelimkhan.magomadov.contactsrevive.domain.contacts

import java.io.File

interface FileRepository {
    suspend fun save(content: String, name: String): File

    suspend fun read(path: String): String
}