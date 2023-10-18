package ly.david.data.spotify.api.auth

interface SpotifyAuthStore {
    suspend fun saveAccessToken(accessToken: String)
    suspend fun getAccessToken(): String?
}
