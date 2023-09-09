package ly.david.data.di.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module

private const val SETTINGS_KEY = "settings"

//@InstallIn(SingletonComponent::class)
//@Module
//object PreferencesDataStoreModule {
//
//    // https://medium.com/androiddevelopers/datastore-and-dependency-injection-ea32b95704e3
//    @Provides
//    @Singleton
//    fun providePreferenceDataStore(
//        @ApplicationContext context: Context,
//        @IoDispatcher ioDispatcher: CoroutineDispatcher,
//    ): DataStore<Preferences> {
//        return PreferenceDataStoreFactory.create(
//            corruptionHandler = ReplaceFileCorruptionHandler(
//                produceNewData = { emptyPreferences() }
//            ),
//            migrations = listOf(SharedPreferencesMigration(context, SETTINGS_KEY)),
//            scope = CoroutineScope(SupervisorJob() + ioDispatcher),
//            produceFile = { context.preferencesDataStoreFile(SETTINGS_KEY) }
//        )
//    }
//}

val preferencesDataStoreModule = module {
    single< DataStore<Preferences>> {
        PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            migrations = listOf(SharedPreferencesMigration(get(), SETTINGS_KEY)),
            scope = CoroutineScope(SupervisorJob() + Dispatchers.IO),
            produceFile = { get<Context>().preferencesDataStoreFile(SETTINGS_KEY) }
        )
    }
}
