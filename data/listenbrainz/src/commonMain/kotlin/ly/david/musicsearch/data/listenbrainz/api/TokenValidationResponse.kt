package ly.david.musicsearch.data.listenbrainz.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenValidationResponse(
    val valid: Boolean,

    @SerialName("user_name")
    val userName: String? = null,
)
