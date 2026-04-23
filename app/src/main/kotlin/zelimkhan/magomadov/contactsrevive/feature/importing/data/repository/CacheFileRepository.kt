package zelimkhan.magomadov.contactsrevive.feature.importing.data.repository

import android.content.Context
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import zelimkhan.magomadov.contactsrevive.feature.importing.domain.repository.FileRepository
import java.io.File
import java.io.InputStreamReader

class CacheFileRepository(
    private val context: Context,
) : FileRepository {
    override suspend fun save(content: String, name: String): File = withContext(Dispatchers.IO) {
        File(context.cacheDir, name).also { it.writeText(content) }
    }

    override suspend fun read(uri: String): String {
        return withContext(Dispatchers.IO) {
            context.contentResolver
                .openInputStream(uri.toUri())
                ?.use { InputStreamReader(it).readText() }
                ?: error("Cannot open URI: $uri")
        }
    }
}