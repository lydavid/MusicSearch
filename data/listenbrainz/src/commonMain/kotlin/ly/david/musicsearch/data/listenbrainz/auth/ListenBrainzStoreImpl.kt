package ly.david.musicsearch.data.listenbrainz.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.shared.domain.auth.ListenBrainzStore

private val usernamePreference = stringPreferencesKey("LISTENBRAINZ_USERNAME")

class ListenBrainzStoreImpl(
    private val preferencesDataStore: DataStore<Preferences>,
) : ListenBrainzStore {

    override val username: Flow<String>
        get() = preferencesDataStore.data
            .map {
                it[usernamePreference].orEmpty()
            }
            .distinctUntilChanged()

    override suspend fun setUsername(username: String) {
        preferencesDataStore.edit {
            it[usernamePreference] = username
        }
    }
}
