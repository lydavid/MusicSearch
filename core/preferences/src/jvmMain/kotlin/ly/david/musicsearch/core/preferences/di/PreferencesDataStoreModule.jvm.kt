package ly.david.musicsearch.core.preferences.di

import ly.david.musicsearch.core.preferences.internal.createDataStore
import ly.david.musicsearch.shared.domain.PACKAGE_NAME
import me.sujanpoudel.utils.paths.appCacheDirectory
import org.koin.core.module.Module
import org.koin.dsl.module

actual val preferencesDataStoreModule: Module = module {
    single {
        val directory = appCacheDirectory(PACKAGE_NAME)
        createDataStore(
            dispatchers = get(),
            path = "$directory/$DATASTORE_FILE_NAME",
        )
    }
}
