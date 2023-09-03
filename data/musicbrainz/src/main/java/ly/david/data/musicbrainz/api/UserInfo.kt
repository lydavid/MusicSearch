package ly.david.data.musicbrainz.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    @SerialName("sub")
    val username: String? = null,
)
