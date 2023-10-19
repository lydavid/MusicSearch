package ly.david.musicsearch.data.musicbrainz.models.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    @SerialName("sub")
    val username: String? = null,
)
