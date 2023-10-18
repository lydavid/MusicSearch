package ly.david.data.spotify.auth.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpotifyOAuthClientCredentialsResponse(
    @SerialName("access_token") val accessToken: String,
)
