package ly.david.musicsearch.data.spotify.auth.store

import kotlinx.coroutines.flow.Flow

interface SpotifyAuthStore {
    suspend fun setAccessToken(accessToken: String)
    suspend fun getAccessToken(): String?

    suspend fun setUserSpotifyClientId(clientId: String)
    suspend fun getUserSpotifyClientId(): String
    suspend fun setUserSpotifyClientSecret(clientSecret: String)
    suspend fun getUserSpotifyClientSecret(): String
    fun observeUserSpotifyAuthSet(): Flow<Boolean>
}
