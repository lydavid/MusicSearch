package ly.david.musicsearch.shared.domain.auth

import kotlinx.coroutines.flow.Flow

interface MusicBrainzAuthStore {
    suspend fun getAccessToken(): String?
    val accessToken: Flow<String?>

    suspend fun getRefreshToken(): String?

    suspend fun getExpiryTimeInEpochSeconds(): Long?

    suspend fun saveTokens(
        accessToken: String,
        refreshToken: String,
    )

    val username: Flow<String>
    suspend fun setUsername(username: String)
}
