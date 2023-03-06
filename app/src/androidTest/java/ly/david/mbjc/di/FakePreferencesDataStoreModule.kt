package ly.david.mbjc.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import java.util.Random
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import ly.david.data.di.PreferencesDataStoreModule

private const val TEST_SETTINGS_KEY = "test_settings"

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [PreferencesDataStoreModule::class]
)
internal object FakePreferencesDataStoreModule {

    @Provides
    @Singleton
    fun providePreferenceDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> {
        // TODO: can this cause collisions if we decide to instrument test datastore?
        val random = Random().nextInt() // https://stackoverflow.com/a/73682506
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            migrations = listOf(SharedPreferencesMigration(context, TEST_SETTINGS_KEY)),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { context.preferencesDataStoreFile("${TEST_SETTINGS_KEY}_$random") }
        )
    }
}
