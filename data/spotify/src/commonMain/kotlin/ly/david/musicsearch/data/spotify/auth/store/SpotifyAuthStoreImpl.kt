package ly.david.musicsearch.data.spotify.auth.store

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val accessTokenPreference = stringPreferencesKey("SPOTIFY_ACCESS_TOKEN_KEY")
private val userSpotifyClientIdPreference = stringPreferencesKey("USER_SPOTIFY_CLIENT_ID")
private val userSpotifyClientSecretPreference = stringPreferencesKey("USER_SPOTIFY_CLIENT_SECRET")

class SpotifyAuthStoreImpl(
    private val preferencesDataStore: DataStore<Preferences>,
) : SpotifyAuthStore {

    override suspend fun setAccessToken(accessToken: String) {
        preferencesDataStore.edit {
            it[accessTokenPreference] = accessToken
        }
    }

    override suspend fun getAccessToken(): String? {
        val preferences = preferencesDataStore.data.first()
        return preferences[accessTokenPreference]
    }

    override suspend fun setUserSpotifyClientId(clientId: String) {
        preferencesDataStore.edit {
            it[userSpotifyClientIdPreference] = clientId
        }
    }

    override suspend fun getUserSpotifyClientId(): String {
        val preferences = preferencesDataStore.data.first()
        return preferences[userSpotifyClientIdPreference].orEmpty()
    }

    override suspend fun setUserSpotifyClientSecret(clientSecret: String) {
        preferencesDataStore.edit {
            it[userSpotifyClientSecretPreference] = clientSecret
        }
    }

    override suspend fun getUserSpotifyClientSecret(): String {
        val preferences = preferencesDataStore.data.first()
        return preferences[userSpotifyClientSecretPreference].orEmpty()
    }

    override fun observeUserSpotifyAuthSet(): Flow<Boolean> {
        return preferencesDataStore.data.map {
            !it[userSpotifyClientIdPreference].isNullOrEmpty() &&
                !it[userSpotifyClientSecretPreference].isNullOrEmpty()
        }
    }
}
