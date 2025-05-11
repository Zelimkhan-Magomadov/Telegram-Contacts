package zelimkhan.magomadov.contactsrevive.data.repository

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import zelimkhan.magomadov.contactsrevive.domain.contacts.FileRepository
import java.io.File
import java.io.InputStreamReader
import javax.inject.Inject

class CacheFileRepository @Inject constructor(
    @ApplicationContext private val context: Context
) : FileRepository {
    override suspend fun save(content: String, name: String): File {
        return File(context.cacheDir, name).apply { writeText(content) }
    }

    override suspend fun read(path: String): String {
        return withContext(Dispatchers.IO) {
            val uri = Uri.parse(path)
            val inputStream = context.contentResolver.openInputStream(uri)
            val inputStreamReader = InputStreamReader(inputStream)
            inputStreamReader.readText()
        }
    }
}