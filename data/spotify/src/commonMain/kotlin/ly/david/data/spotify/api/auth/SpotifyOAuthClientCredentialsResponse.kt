package ly.david.data.spotify.api.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpotifyOAuthClientCredentialsResponse(
    @SerialName("access_token") val accessToken: String,
)
