package ly.david.musicsearch.data.musicbrainz.models.core

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ly.david.musicsearch.core.models.label.Label
import ly.david.musicsearch.data.musicbrainz.models.common.LifeSpanMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.relation.RelationMusicBrainzModel

@Serializable
data class LabelMusicBrainzModel(
    @SerialName("id") override val id: String,
    @SerialName("name") override val name: String,
    @SerialName("disambiguation") override val disambiguation: String? = null,
    @SerialName("type") override val type: String? = null,
    @SerialName("type-id") val typeId: String? = null,
    @SerialName("label-code") override val labelCode: Int? = null,
    @SerialName("life-span") val lifeSpan: LifeSpanMusicBrainzModel? = null,

    @SerialName("area") val area: AreaMusicBrainzModel? = null,
    @SerialName("relations") val relations: List<RelationMusicBrainzModel>? = null,
) : MusicBrainzModel(), Label
