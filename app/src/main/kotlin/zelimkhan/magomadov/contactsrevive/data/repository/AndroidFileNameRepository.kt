package zelimkhan.magomadov.contactsrevive.data.repository

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import dagger.hilt.android.qualifiers.ApplicationContext
import zelimkhan.magomadov.contactsrevive.domain.fileName.FileNameRepository
import javax.inject.Inject

class AndroidFileNameRepository @Inject constructor(
    @ApplicationContext private val context: Context
) : FileNameRepository {
    override suspend fun name(path: String): String {
        val uri = Uri.parse(path)
        val cursor = context.contentResolver.query(uri, null, null, null, null)!!
        cursor.use {
            it.moveToFirst()
            return it.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
        }
    }
}