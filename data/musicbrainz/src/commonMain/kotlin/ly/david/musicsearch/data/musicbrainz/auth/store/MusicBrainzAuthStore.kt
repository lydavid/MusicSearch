package ly.david.musicsearch.data.musicbrainz.auth.store

import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.data.musicbrainz.auth.AccessToken

interface MusicBrainzAuthStore {
    suspend fun getAccessToken(): String?
    val accessToken: Flow<String?>
    suspend fun getRefreshToken(): String?
    suspend fun getExpiryTimeInEpochSeconds(): Long?
    suspend fun saveTokens(accessToken: AccessToken)
    val username: Flow<String>
    suspend fun setUsername(username: String)
}
