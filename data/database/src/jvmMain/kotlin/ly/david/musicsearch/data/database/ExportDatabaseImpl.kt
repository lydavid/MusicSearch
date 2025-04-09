package ly.david.musicsearch.data.database

import kotlinx.io.IOException
import ly.david.musicsearch.shared.domain.ExportDatabase
import ly.david.musicsearch.shared.domain.PACKAGE_NAME
import me.sujanpoudel.utils.paths.appCacheDirectory
import okio.FileSystem
import okio.Path.Companion.toPath

internal class ExportDatabaseImpl : ExportDatabase {
    override operator fun invoke(): String {
        return try {
            val databasePath = "${appCacheDirectory(PACKAGE_NAME)}/$DATABASE_FILE_FULL_NAME".toPath()
            val destinationPath = "./$exportFileName".toPath()
            FileSystem.SYSTEM.copy(
                source = databasePath,
                target = destinationPath,
            )
            "Successfully exported database to ${FileSystem.SYSTEM.canonicalize(destinationPath)}!"
        } catch (ex: IOException) {
            "Error: $ex"
        }
    }
}
