package ly.david.mbjc.di

import android.content.Context
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import java.util.Random
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import ly.david.musicsearch.data.core.CoroutineDispatchers
import org.koin.dsl.module

private const val TEST_SETTINGS_KEY = "test_settings"

val testPreferencesDataStoreModule = module {
    single {
        val random = Random().nextInt() // https://stackoverflow.com/a/73682506
        PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            migrations = listOf(SharedPreferencesMigration(get(), TEST_SETTINGS_KEY)),
            scope = CoroutineScope(SupervisorJob() + get<CoroutineDispatchers>().io),
            produceFile = { get<Context>().preferencesDataStoreFile("${TEST_SETTINGS_KEY}_$random") }
        )
    }
}
