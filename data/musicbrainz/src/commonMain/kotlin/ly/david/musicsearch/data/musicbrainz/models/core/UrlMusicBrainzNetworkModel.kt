package ly.david.musicsearch.data.musicbrainz.models.core

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ly.david.musicsearch.data.musicbrainz.models.relation.RelationMusicBrainzModel

@Serializable
data class UrlMusicBrainzNetworkModel(
    @SerialName("relations") val relations: List<RelationMusicBrainzModel>? = null,
)
