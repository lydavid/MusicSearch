package ly.david.mbjc.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import ly.david.mbjc.ui.settings.AppPreferences
import ly.david.mbjc.ui.settings.AppPreferencesImpl

private const val SETTINGS_KEY = "settings"

@InstallIn(SingletonComponent::class)
@Module
internal object PreferencesModule {

    // https://medium.com/androiddevelopers/datastore-and-dependency-injection-ea32b95704e3
    @Provides
    @Singleton
    fun providePreferenceDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            migrations = listOf(SharedPreferencesMigration(context, SETTINGS_KEY)),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { context.preferencesDataStoreFile(SETTINGS_KEY) }
        )
    }
}

@InstallIn(SingletonComponent::class)
@Module
internal abstract class PreferencesModuleBinds {
    @Singleton
    @Binds
    abstract fun provideAppPreferences(bind: AppPreferencesImpl): AppPreferences
}
