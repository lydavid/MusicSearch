package ly.david.data.di.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import ly.david.musicsearch.data.musicbrainz.auth.MusicBrainzAuthStore
import org.koin.core.annotation.Single
import kotlin.time.Duration.Companion.hours

private val accessTokenPreference = stringPreferencesKey("MUSICBRAINZ_ACCESS_TOKEN")
private val refreshTokenPreference = stringPreferencesKey("MUSICBRAINZ_REFRESH_TOKEN")
private val expiryTimePreference = longPreferencesKey("MUSICBRAINZ_EXPIRY_TIME")
private val usernamePreference = stringPreferencesKey("MUSICBRAINZ_USERNAME")

// TODO: should this be a feature?
@Single(binds = [MusicBrainzAuthStore::class])
class MusicBrainzAuthStoreImpl(
    private val preferencesDataStore: DataStore<Preferences>,
    private val coroutineScope: CoroutineScope,
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

    override fun saveTokens(
        accessToken: String,
        refreshToken: String,
    ) {
        coroutineScope.launch {
            preferencesDataStore.edit {
                it[accessTokenPreference] = accessToken
                it[refreshTokenPreference] = refreshToken
                it[expiryTimePreference] = Clock.System.now().plus(1.hours).epochSeconds
            }
        }
    }

    override val username: Flow<String>
        get() = preferencesDataStore.data
            .map {
                it[usernamePreference].orEmpty()
            }
            .distinctUntilChanged()

    override fun setUsername(username: String) {
        coroutineScope.launch {
            preferencesDataStore.edit {
                it[usernamePreference] = username
            }
        }
    }
}
