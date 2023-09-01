package ly.david.data.network

import kotlinx.coroutines.flow.Flow

interface MusicBrainzAuthState {
    suspend fun getAccessToken(): String?
    val accessToken: Flow<String?>
    suspend fun getRefreshToken(): String?
    fun saveTokens(accessToken: String, refreshToken: String)

    val username: Flow<String>
    fun setUsername(username: String)
}
