package ly.david.musicsearch.data.database

import co.touchlab.sqliter.DatabaseFileContext
import kotlinx.coroutines.withContext
import kotlinx.io.IOException
import ly.david.musicsearch.shared.domain.ExportDatabase
import ly.david.musicsearch.shared.domain.common.getIosPathStringFor
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import okio.FileSystem
import okio.Path.Companion.toPath
import platform.Foundation.NSDocumentDirectory
import kotlin.time.Clock

internal class ExportDatabaseImpl(
    private val coroutineDispatchers: CoroutineDispatchers,
    private val clock: Clock,
) : ExportDatabase {
    override suspend operator fun invoke(): String {
        return try {
            withContext(coroutineDispatchers.io) {
                // https://github.com/sqldelight/sqldelight/discussions/3248
                val databasePathString = DatabaseFileContext.databasePath(
                    databaseName = DATABASE_FILE_FULL_NAME,
                    datapathPath = null,
                )

                // Put file in Documents if we want to expose it to the user
                // https://developer.apple.com/library/archive/documentation/FileManagement/Conceptual/FileSystemProgrammingGuide/FileSystemOverview/FileSystemOverview.html#//apple_ref/doc/uid/TP40010672-CH2-SW28
                val downloadsPathString = getIosPathStringFor(
                    directory = NSDocumentDirectory,
                )
                val destinationPathString = "$downloadsPathString/${getExportFileName(clock = clock)}"

                FileSystem.SYSTEM.copy(
                    databasePathString.toPath(),
                    destinationPathString.toPath(),
                )

                // https://stackoverflow.com/a/70613710
                // With both of these options, we can let the user view the app's Documents directory in Files
                // https://developer.apple.com/documentation/bundleresources/information-property-list/uifilesharingenabled
                // https://developer.apple.com/documentation/bundleresources/information-property-list/lssupportsopeningdocumentsinplace
                "Successfully exported database to $destinationPathString"
            }
        } catch (ex: IOException) {
            "Error: $ex"
        }
    }
}
