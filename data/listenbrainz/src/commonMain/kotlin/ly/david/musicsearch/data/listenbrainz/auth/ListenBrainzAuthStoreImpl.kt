package ly.david.musicsearch.data.listenbrainz.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.shared.domain.listen.ListenBrainzAuthStore

// TODO: use an enum for all preferences name
private val userTokenPreference = stringPreferencesKey("LISTENBRAINZ_USER_TOKEN")
private val usernamePreference = stringPreferencesKey("LISTENBRAINZ_USER_USERNAME")

// backwards-compatible name
private val browseUsernamePreference = stringPreferencesKey("LISTENBRAINZ_USERNAME")

class ListenBrainzAuthStoreImpl(
    private val preferencesDataStore: DataStore<Preferences>,
) : ListenBrainzAuthStore {
    override suspend fun getUserToken(): String {
        val preferences = preferencesDataStore.data.first()
        return preferences[userTokenPreference].orEmpty()
    }

    override fun observeUserToken(): Flow<String> {
        return preferencesDataStore.data
            .map { preferences ->
                preferences[userTokenPreference].orEmpty()
            }
            .distinctUntilChanged()
    }

    override suspend fun setUserToken(token: String) {
        preferencesDataStore.edit {
            it[userTokenPreference] = token
        }
    }

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

    override val browseUsername: Flow<String>
        get() = preferencesDataStore.data
            .map {
                it[browseUsernamePreference].orEmpty()
            }
            .distinctUntilChanged()

    override suspend fun setBrowseUsername(username: String) {
        preferencesDataStore.edit {
            it[browseUsernamePreference] = username
        }
    }
}
