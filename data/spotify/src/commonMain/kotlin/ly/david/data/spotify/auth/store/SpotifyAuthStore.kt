package ly.david.data.spotify.auth.store

interface SpotifyAuthStore {
    suspend fun saveAccessToken(accessToken: String)
    suspend fun getAccessToken(): String?
}
