package ly.david.data.spotify.auth

interface SpotifyOAuth {
    fun saveAccessToken(accessToken: String)
    suspend fun getAccessToken(): String?
}
