package ly.david.data.di.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ly.david.data.spotify.api.auth.SpotifyAuthStore
import org.koin.core.annotation.Single

private val accessTokenPreference = stringPreferencesKey("SPOTIFY_ACCESS_TOKEN_KEY")

@Single(binds = [SpotifyAuthStore::class])
class SpotifyAuthStoreImpl(
    private val preferencesDataStore: DataStore<Preferences>,
    private val coroutineScope: CoroutineScope,
) : SpotifyAuthStore {

    override fun saveAccessToken(accessToken: String) {
        coroutineScope.launch {
            preferencesDataStore.edit {
                it[accessTokenPreference] = accessToken
            }
        }
    }

    override suspend fun getAccessToken(): String? {
        val preferences = preferencesDataStore.data.first()
        return preferences[accessTokenPreference]
    }
}
