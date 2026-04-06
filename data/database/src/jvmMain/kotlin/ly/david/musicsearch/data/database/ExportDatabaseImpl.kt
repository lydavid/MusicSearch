package ly.david.musicsearch.data.database

import kotlinx.coroutines.withContext
import kotlinx.io.IOException
import ly.david.musicsearch.shared.domain.ExportDatabase
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import okio.FileSystem
import okio.Path.Companion.toPath
import kotlin.time.Clock

internal class ExportDatabaseImpl(
    private val coroutineDispatchers: CoroutineDispatchers,
    private val clock: Clock,
) : ExportDatabase {
    override suspend operator fun invoke(): String {
        return try {
            withContext(coroutineDispatchers.io) {
                val databasePath = databasePath.toPath()
                val destinationPath = "./${getExportFileName(clock = clock)}".toPath()
                FileSystem.SYSTEM.copy(
                    source = databasePath,
                    target = destinationPath,
                )
                "Successfully exported database to ${FileSystem.SYSTEM.canonicalize(destinationPath)}"
            }
        } catch (ex: IOException) {
            "Error: $ex"
        }
    }
}
