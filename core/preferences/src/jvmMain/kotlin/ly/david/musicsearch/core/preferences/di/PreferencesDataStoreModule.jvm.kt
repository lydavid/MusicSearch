package ly.david.musicsearch.core.preferences.di

import ly.david.musicsearch.core.preferences.internal.createDataStore
import org.koin.core.module.Module
import org.koin.dsl.module

actual val preferencesDataStoreModule: Module = module {
    single {
        createDataStore(
            dispatchers = get(),
            path = DATASTORE_FILE_NAME,
        )
    }
}
