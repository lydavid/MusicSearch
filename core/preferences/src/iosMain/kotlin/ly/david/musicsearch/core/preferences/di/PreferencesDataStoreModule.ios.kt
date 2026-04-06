package ly.david.musicsearch.core.preferences.di

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import kotlinx.cinterop.ExperimentalForeignApi
import ly.david.musicsearch.shared.domain.common.getIosPathStringFor
import okio.Path.Companion.toPath
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSLibraryDirectory

@OptIn(ExperimentalForeignApi::class)
actual val preferencesDataStoreModule: Module = module {
    single {
        PreferenceDataStoreFactory.createWithPath {

            // Put file in Library if we do not want to expose to it the user
            // https://developer.apple.com/library/archive/documentation/FileManagement/Conceptual/FileSystemProgrammingGuide/FileSystemOverview/FileSystemOverview.html
            val documentDirectoryPathString = getIosPathStringFor(
                directory = NSLibraryDirectory,
            )

            "$documentDirectoryPathString/$DATASTORE_FILE_NAME".toPath()
        }
    }
}
