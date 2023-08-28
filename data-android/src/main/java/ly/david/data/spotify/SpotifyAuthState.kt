package ly.david.data.spotify

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ly.david.data.common.ApplicationScope
import ly.david.data.spotify.auth.SpotifyOAuth

private val accessTokenPreference = stringPreferencesKey("SPOTIFY_ACCESS_TOKEN_KEY")
private val refreshTokenPreference = stringPreferencesKey("SPOTIFY_REFRESH_TOKEN_KEY")
private val expirationTimePreference = longPreferencesKey("SPOTIFY_ACCESS_TOKEN_EXPIRATION_KEY")

class SpotifyOAuthImpl @Inject constructor(
    private val preferencesDataStore: DataStore<Preferences>,
    @ApplicationScope private val coroutineScope: CoroutineScope,
) : SpotifyOAuth {

    override fun saveAccessToken(
        accessToken: String,
        refreshToken: String,
        expirationSystemTime: Long,
    ) {
        coroutineScope.launch {
            preferencesDataStore.edit {
                it[accessTokenPreference] = accessToken
                it[refreshTokenPreference] = refreshToken
                it[expirationTimePreference] = expirationSystemTime
            }
        }
    }

    override suspend fun getAccessToken(): String? {
        val preferences = preferencesDataStore.data.first()
        return preferences[accessTokenPreference]
    }

    override suspend fun getRefreshToken(): String? {
        val preferences = preferencesDataStore.data.first()
        return preferences[refreshTokenPreference]
    }

    override suspend fun getExpirationTime(): Long? {
        val preferences = preferencesDataStore.data.first()
        return preferences[expirationTimePreference]
    }
}
