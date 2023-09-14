package ly.david.data.musicbrainz.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MusicBrainzOAuthResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("refresh_token") val refreshToken: String,
)
