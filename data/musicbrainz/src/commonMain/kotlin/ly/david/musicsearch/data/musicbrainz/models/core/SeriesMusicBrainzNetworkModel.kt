package ly.david.musicsearch.data.musicbrainz.models.core

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ly.david.musicsearch.data.musicbrainz.models.common.AliasMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.relation.RelationMusicBrainzModel
import ly.david.musicsearch.shared.domain.series.Series

@Serializable
data class SeriesMusicBrainzNetworkModel(
    @SerialName("id") override val id: String,
    @SerialName("name") override val name: String,
    @SerialName("disambiguation") override val disambiguation: String? = null,
    @SerialName("type") override val type: String? = null,
    @SerialName("type-id") val typeId: String? = null,

    @SerialName("relations") val relations: List<RelationMusicBrainzModel>? = null,
    @SerialName("aliases") val aliases: List<AliasMusicBrainzNetworkModel>? = null,
) : MusicBrainzNetworkModel(), Series
