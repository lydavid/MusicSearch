package ly.david.musicsearch.data.musicbrainz.auth

import kotlinx.coroutines.flow.Flow

interface MusicBrainzAuthStore {
    suspend fun getAccessToken(): String?
    val accessToken: Flow<String?>
    suspend fun getRefreshToken(): String?
    suspend fun getExpiryTimeInEpochSeconds(): Long?
    fun saveTokens(
        accessToken: String,
        refreshToken: String,
    )

    val username: Flow<String>
    fun setUsername(username: String)
}
