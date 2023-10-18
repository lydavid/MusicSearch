package ly.david.data.spotify.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import ly.david.data.spotify.api.auth.SpotifyAuthStore

private val accessTokenPreference = stringPreferencesKey("SPOTIFY_ACCESS_TOKEN_KEY")

class SpotifyAuthStoreImpl(
    private val preferencesDataStore: DataStore<Preferences>,
) : SpotifyAuthStore {

    override suspend fun saveAccessToken(accessToken: String) {
        preferencesDataStore.edit {
            it[accessTokenPreference] = accessToken
        }
    }

    override suspend fun getAccessToken(): String? {
        val preferences = preferencesDataStore.data.first()
        return preferences[accessTokenPreference]
    }
}
