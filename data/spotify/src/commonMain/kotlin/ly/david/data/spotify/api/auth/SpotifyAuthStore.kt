package ly.david.data.spotify.api.auth

interface SpotifyAuthStore {
    fun saveAccessToken(accessToken: String)
    suspend fun getAccessToken(): String?
}
