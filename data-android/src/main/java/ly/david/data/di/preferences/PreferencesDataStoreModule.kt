package ly.david.data.di.preferences

import android.content.Context
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import ly.david.data.di.coroutines.MusicSearchDispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val SETTINGS_KEY = "settings"

val preferencesDataStoreModule = module {
    single {
        PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            migrations = listOf(SharedPreferencesMigration(get(), SETTINGS_KEY)),
            scope = CoroutineScope(SupervisorJob() + get<CoroutineDispatcher>(named(MusicSearchDispatchers.IO))),
            produceFile = { get<Context>().preferencesDataStoreFile(SETTINGS_KEY) }
        )
    }
}
