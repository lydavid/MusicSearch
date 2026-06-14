package ly.david.musicsearch.data.database

import kotlinx.coroutines.withContext
import kotlinx.io.IOException
import ly.david.musicsearch.shared.domain.ExportDatabase
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import okio.FileSystem
import okio.Path.Companion.toPath
import javax.swing.JFileChooser
import kotlin.time.Clock

internal class ExportDatabaseImpl(
    private val coroutineDispatchers: CoroutineDispatchers,
    private val clock: Clock,
) : ExportDatabase {
    override suspend operator fun invoke(): String {
        return try {
            val chooser = JFileChooser().apply {
                fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
                dialogTitle = "Choose export folder"
            }
            if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
                return "You need to select a folder to save the database to."
            }

            withContext(coroutineDispatchers.io) {
                val databasePath = databasePath.toPath()

                val folder = chooser.selectedFile.absolutePath
                val destinationPath = "$folder/${getExportFileName(clock = clock)}".toPath()

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
