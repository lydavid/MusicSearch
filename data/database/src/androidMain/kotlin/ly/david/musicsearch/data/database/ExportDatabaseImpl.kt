package ly.david.musicsearch.data.database

import android.content.Context
import android.os.Environment
import kotlinx.coroutines.withContext
import ly.david.musicsearch.shared.domain.ExportDatabase
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

internal class ExportDatabaseImpl(
    private val context: Context,
    private val coroutineDispatchers: CoroutineDispatchers,
) : ExportDatabase {
    override suspend operator fun invoke(): String {
        return try {
            withContext(coroutineDispatchers.io) {
                val databasePath = context.getDatabasePath(DATABASE_FILE_FULL_NAME)
                val downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val destinationFile = File(downloadsDirectory, exportFileName)
                FileInputStream(databasePath).use { inputStream ->
                    FileOutputStream(destinationFile).use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
                "Successfully exported database to ${destinationFile.absolutePath}!"
            }
        } catch (ex: IOException) {
            "Error: $ex"
        }
    }
}
