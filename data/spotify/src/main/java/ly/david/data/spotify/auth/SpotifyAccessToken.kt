package ly.david.data.spotify.auth

import com.squareup.moshi.Json

data class SpotifyAccessToken(
    @Json(name = "access_token") val accessToken: String,
    @Json(name = "expires_in") val expiresIn: Long
)
