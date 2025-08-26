package ly.david.musicsearch.data.database

import kotlinx.coroutines.withContext
import kotlinx.io.IOException
import ly.david.musicsearch.shared.domain.ExportDatabase
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import okio.FileSystem
import okio.Path.Companion.toPath

internal class ExportDatabaseImpl(
    private val coroutineDispatchers: CoroutineDispatchers,
) : ExportDatabase {
    override suspend operator fun invoke(): String {
        return try {
            withContext(coroutineDispatchers.io) {
                val databasePath = databasePath.toPath()
                val destinationPath = "./$exportFileName".toPath()
                FileSystem.SYSTEM.copy(
                    source = databasePath,
                    target = destinationPath,
                )
                "Successfully exported database to ${FileSystem.SYSTEM.canonicalize(destinationPath)}!"
            }
        } catch (ex: IOException) {
            "Error: $ex"
        }
    }
}
