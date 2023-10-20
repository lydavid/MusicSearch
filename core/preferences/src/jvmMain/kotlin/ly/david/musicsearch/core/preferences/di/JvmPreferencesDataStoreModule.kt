package ly.david.musicsearch.core.preferences.di

import ly.david.musicsearch.core.preferences.internal.SETTINGS_KEY
import ly.david.musicsearch.core.preferences.internal.createDataStore
import org.koin.core.module.Module
import org.koin.dsl.module

actual val preferencesDataStoreModule: Module = module {
    single {
        createDataStore(
            dispatchers = get(),
            path = "datastore/$SETTINGS_KEY.preferences_pb",
        )
    }
}
