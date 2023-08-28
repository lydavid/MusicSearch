package ly.david.data.spotify.api.auth

interface SpotifyOAuth {
    fun saveAccessToken(accessToken: String)
    suspend fun getAccessToken(): String?
}
