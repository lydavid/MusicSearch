package ly.david.musicsearch.core.preferences.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStoreFile
import ly.david.musicsearch.core.preferences.internal.SETTINGS_KEY
import ly.david.musicsearch.core.preferences.internal.createDataStore
import org.koin.core.module.Module
import org.koin.dsl.module

// File named differently due to: https://youtrack.jetbrains.com/issue/KT-21186
actual val preferencesDataStoreModule: Module = module {
    single {
        createDataStore(
            dispatchers = get(),
            path = get<Context>().preferencesDataStoreFile(SETTINGS_KEY).absolutePath,
        )
    }
}
