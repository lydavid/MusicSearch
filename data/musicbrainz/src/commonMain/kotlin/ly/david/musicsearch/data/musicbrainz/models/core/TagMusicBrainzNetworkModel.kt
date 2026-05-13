package ly.david.musicsearch.data.musicbrainz.models.core

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TagMusicBrainzNetworkModel(
    @SerialName("name") val name: String,
    @SerialName("count") val count: Int? = null,
)
