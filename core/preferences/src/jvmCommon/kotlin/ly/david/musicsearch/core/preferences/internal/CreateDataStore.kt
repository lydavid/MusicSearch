package ly.david.musicsearch.core.preferences.internal

import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import java.io.File


internal fun createDataStore(
    dispatchers: CoroutineDispatchers,
    path: String,
): DataStore<Preferences> = PreferenceDataStoreFactory.create(
    corruptionHandler = ReplaceFileCorruptionHandler(
        produceNewData = { emptyPreferences() },
    ),
    migrations = emptyList(),
    scope = CoroutineScope(SupervisorJob() + dispatchers.io),
    produceFile = { File(path) },
)
