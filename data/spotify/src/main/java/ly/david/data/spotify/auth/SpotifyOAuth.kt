package ly.david.data.spotify.auth

interface SpotifyOAuth {
    fun saveAccessToken(
        accessToken: String,
        refreshToken: String,
        expirationSystemTime: Long,
    )
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun getExpirationTime(): Long?
}
