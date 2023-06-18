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
import ly.david.data.di.ApplicationScope

private const val SPOTIFY_ACCESS_TOKEN_KEY = "SPOTIFY_ACCESS_TOKEN_KEY"
private val ACCESS_TOKEN_PREFERENCE = stringPreferencesKey(SPOTIFY_ACCESS_TOKEN_KEY)
private const val SPOTIFY_ACCESS_TOKEN_EXPIRATION_KEY = "SPOTIFY_ACCESS_TOKEN_EXPIRATION_KEY"
private val SPOTIFY_ACCESS_TOKEN_EXPIRATION_PREFERENCE = longPreferencesKey(SPOTIFY_ACCESS_TOKEN_EXPIRATION_KEY)

interface SpotifyOAuth {
    fun saveAccessToken(accessToken: String, expirationSystemTime: Long)

    suspend fun getAccessToken(): String?
    suspend fun getExpirationTime(): Long?
}

class SpotifyOAuthImpl @Inject constructor(
    private val preferencesDataStore: DataStore<Preferences>,
    @ApplicationScope private val coroutineScope: CoroutineScope
) : SpotifyOAuth {

    override suspend fun getAccessToken(): String? {
        val preferences = preferencesDataStore.data.first()
        return preferences[ACCESS_TOKEN_PREFERENCE]
    }

    override fun saveAccessToken(
        accessToken: String,
        expirationSystemTime: Long
    ) {
        coroutineScope.launch {
            preferencesDataStore.edit {
                it[ACCESS_TOKEN_PREFERENCE] = accessToken
                it[SPOTIFY_ACCESS_TOKEN_EXPIRATION_PREFERENCE] = expirationSystemTime
            }
        }
    }

    override suspend fun getExpirationTime(): Long? {
        val preferences = preferencesDataStore.data.first()
        return preferences[SPOTIFY_ACCESS_TOKEN_EXPIRATION_PREFERENCE]
    }
}
