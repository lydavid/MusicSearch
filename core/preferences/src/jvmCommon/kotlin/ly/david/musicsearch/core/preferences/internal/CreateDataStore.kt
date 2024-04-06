package ly.david.musicsearch.core.preferences.internal

import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import ly.david.musicsearch.core.coroutines.CoroutineDispatchers
import java.io.File

internal const val SETTINGS_KEY = "settings"

internal fun createDataStore(
    dispatchers: CoroutineDispatchers,
    path: String,
) = PreferenceDataStoreFactory.create(
    corruptionHandler = ReplaceFileCorruptionHandler(
        produceNewData = { emptyPreferences() },
    ),
    migrations = emptyList(),
    scope = CoroutineScope(SupervisorJob() + dispatchers.io),
    produceFile = { File(path) },
)
