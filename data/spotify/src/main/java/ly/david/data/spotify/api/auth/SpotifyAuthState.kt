package ly.david.data.spotify.api.auth

interface SpotifyAuthState {
    fun saveAccessToken(accessToken: String)
    suspend fun getAccessToken(): String?
}
