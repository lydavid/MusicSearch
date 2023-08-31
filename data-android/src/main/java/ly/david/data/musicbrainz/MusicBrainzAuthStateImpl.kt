package ly.david.data.musicbrainz

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ly.david.data.BEARER
import ly.david.data.common.ApplicationScope
import ly.david.data.common.emptyToNull
import ly.david.data.common.transformThisIfNotNullOrEmpty
import ly.david.data.network.MusicBrainzAuthState

private val accessTokenPreference = stringPreferencesKey("MUSICBRAINZ_ACCESS_TOKEN")
private val refreshTokenPreference = stringPreferencesKey("MUSICBRAINZ_REFRESH_TOKEN")
private val usernamePreference = stringPreferencesKey("MUSICBRAINZ_USERNAME")

// TODO: can remove once we stop passing this in through VM
suspend fun MusicBrainzAuthState.getBearerToken(): String? {
    val accessToken = getAccessToken()
    return accessToken?.transformThisIfNotNullOrEmpty { "$BEARER $it" }.emptyToNull()
}

class MusicBrainzAuthStateImpl @Inject constructor(
    private val preferencesDataStore: DataStore<Preferences>,
    @ApplicationScope private val coroutineScope: CoroutineScope,
) : MusicBrainzAuthState {

    override suspend fun getAccessToken(): String? {
        val preferences = preferencesDataStore.data.first()
        return preferences[accessTokenPreference]
    }

    override suspend fun getRefreshToken(): String? {
        val preferences = preferencesDataStore.data.first()
        return preferences[refreshTokenPreference]
    }

    override fun saveTokens(accessToken: String, refreshToken: String) {
        coroutineScope.launch {
            preferencesDataStore.edit {
                it[accessTokenPreference] = accessToken
                it[refreshTokenPreference] = refreshToken
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
