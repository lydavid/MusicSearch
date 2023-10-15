package ly.david.data.musicbrainz

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ly.david.musicsearch.core.models.artist.Artist

@Serializable
data class ArtistMusicBrainzModel(
    @SerialName("id") override val id: String,
    @SerialName("name") override val name: String = "",
    @SerialName("sort-name") override val sortName: String = "",
    @SerialName("disambiguation") override val disambiguation: String? = null,
    @SerialName("type") override val type: String? = null,
    @SerialName("type-id") val typeId: String? = null,
    @SerialName("gender") override val gender: String? = null,
    @SerialName("country") override val countryCode: String? = null,
    @SerialName("life-span") val lifeSpan: LifeSpanMusicBrainzModel? = null,

    @SerialName("relations") val relations: List<RelationMusicBrainzModel>? = null,
) : MusicBrainzModel(), Artist
