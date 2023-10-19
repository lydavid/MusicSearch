package ly.david.musicsearch.data.spotify.auth.store

interface SpotifyAuthStore {
    suspend fun saveAccessToken(accessToken: String)
    suspend fun getAccessToken(): String?
}
