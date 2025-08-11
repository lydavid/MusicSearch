package ly.david.musicsearch.data.musicbrainz.auth.store

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlin.time.Clock
import ly.david.musicsearch.shared.domain.auth.MusicBrainzAuthStore
import kotlin.time.Duration.Companion.hours

private val accessTokenPreference = stringPreferencesKey("MUSICBRAINZ_ACCESS_TOKEN")
private val refreshTokenPreference = stringPreferencesKey("MUSICBRAINZ_REFRESH_TOKEN")
private val expiryTimePreference = longPreferencesKey("MUSICBRAINZ_EXPIRY_TIME")
private val usernamePreference = stringPreferencesKey("MUSICBRAINZ_USERNAME")

class MusicBrainzAuthStoreImpl(
    private val preferencesDataStore: DataStore<Preferences>,
) : MusicBrainzAuthStore {

    override suspend fun getAccessToken(): String? {
        val preferences = preferencesDataStore.data.first()
        return preferences[accessTokenPreference]
    }

    override val accessToken: Flow<String?>
        get() = preferencesDataStore.data
            .map {
                it[accessTokenPreference].orEmpty()
            }
            .distinctUntilChanged()

    override suspend fun getRefreshToken(): String? {
        val preferences = preferencesDataStore.data.first()
        return preferences[refreshTokenPreference]
    }

    override suspend fun getExpiryTimeInEpochSeconds(): Long? {
        val preferences = preferencesDataStore.data.first()
        return preferences[expiryTimePreference]
    }

    override suspend fun saveTokens(
        accessToken: String,
        refreshToken: String,
    ) {
        preferencesDataStore.edit {
            it[accessTokenPreference] = accessToken
            it[refreshTokenPreference] = refreshToken
            it[expiryTimePreference] = Clock.System.now().plus(1.hours).epochSeconds
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
}
