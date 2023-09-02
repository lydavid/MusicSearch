package ly.david.data.di.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ly.david.data.di.ApplicationScope
import ly.david.data.spotify.api.auth.SpotifyAuthState

private val accessTokenPreference = stringPreferencesKey("SPOTIFY_ACCESS_TOKEN_KEY")

class SpotifyAuthStateImpl @Inject constructor(
    private val preferencesDataStore: DataStore<Preferences>,
    @ApplicationScope private val coroutineScope: CoroutineScope,
) : SpotifyAuthState {

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
