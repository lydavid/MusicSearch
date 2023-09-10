package ly.david.mbjc.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import java.util.Random
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import ly.david.data.di.coroutines.MusicSearchDispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val TEST_SETTINGS_KEY = "test_settings"

//@Module
//@TestInstallIn(
//    components = [SingletonComponent::class],
//    replaces = [PreferencesDataStoreModule::class]
//)
//internal object FakePreferencesDataStoreModule {
//
//    @Provides
//    @Singleton
//    fun providePreferenceDataStore(
//        @ApplicationContext context: Context,
//        @IoDispatcher ioDispatcher: CoroutineDispatcher
//    ): DataStore<Preferences> {
//        // TODO: can this cause collisions if we decide to instrument test datastore?
//        val random = Random().nextInt() // https://stackoverflow.com/a/73682506
//        return PreferenceDataStoreFactory.create(
//            corruptionHandler = ReplaceFileCorruptionHandler(
//                produceNewData = { emptyPreferences() }
//            ),
//            migrations = listOf(SharedPreferencesMigration(context, TEST_SETTINGS_KEY)),
//            scope = CoroutineScope(SupervisorJob() + ioDispatcher),
//            produceFile = { context.preferencesDataStoreFile("${TEST_SETTINGS_KEY}_$random") }
//        )
//    }
//}

val testPreferencesDataStoreModule = module {
    single {
        val random = Random().nextInt() // https://stackoverflow.com/a/73682506
        PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            migrations = listOf(SharedPreferencesMigration(get(), TEST_SETTINGS_KEY)),
            scope = CoroutineScope(SupervisorJob() + get<CoroutineDispatcher>(named(MusicSearchDispatchers.IO))),
            produceFile = { get<Context>().preferencesDataStoreFile("${TEST_SETTINGS_KEY}_$random") }
        )
    }
}
