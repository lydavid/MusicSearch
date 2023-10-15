package ly.david.data.musicbrainz

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ly.david.musicsearch.core.models.series.Series

@Serializable
data class SeriesMusicBrainzModel(
    @SerialName("id") override val id: String,
    @SerialName("name") override val name: String,
    @SerialName("disambiguation") override val disambiguation: String? = null,
    @SerialName("type") override val type: String? = null,
    @SerialName("type-id") val typeId: String? = null,

    @SerialName("relations") val relations: List<RelationMusicBrainzModel>? = null,
) : MusicBrainzModel(), Series
