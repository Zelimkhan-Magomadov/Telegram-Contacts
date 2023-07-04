package zelimkhan.magomadov.telegramcontacts.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import zelimkhan.magomadov.telegramcontacts.domain.repository.FileNameRepository
import zelimkhan.magomadov.telegramcontacts.domain.repository.FileRepository
import java.io.File
import java.io.InputStreamReader

class CacheFileRepository(
    @ApplicationContext private val context: Context
) : FileRepository, FileNameRepository {
    override suspend fun createFile(content: String, fileName: String): File {
        return File(context.cacheDir, fileName).apply { writeText(content) }
    }

    override suspend fun getContentFromFile(path: String): String {
        return withContext(Dispatchers.IO) {
            val uri = Uri.parse(path)
            val inputStream = context.contentResolver.openInputStream(uri)
            val inputStreamReader = InputStreamReader(inputStream)
            inputStreamReader.readText()
        }
    }

    @SuppressLint("Range")
    override suspend fun getFileName(path: String): String {
        val uri = Uri.parse(path)
        val cursor = context.contentResolver.query(uri, null, null, null, null)!!
        cursor.use {
            it.moveToFirst()
            return it.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
        }
    }
}