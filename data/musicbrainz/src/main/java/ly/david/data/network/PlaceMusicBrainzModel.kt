package ly.david.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ly.david.data.CoordinatesMusicBrainzModel
import ly.david.data.LifeSpanMusicBrainzModel
import ly.david.data.Place

@Serializable
data class PlaceMusicBrainzModel(
    @SerialName("id") override val id: String,
    @SerialName("name") override val name: String,
    @SerialName("disambiguation") override val disambiguation: String? = null,
    @SerialName("address") override val address: String = "",
    @SerialName("type") override val type: String? = null,
    @SerialName("type-id") val typeId: String? = null,
    @SerialName("coordinates") override val coordinates: CoordinatesMusicBrainzModel? = null,
    @SerialName("life-span") override val lifeSpan: LifeSpanMusicBrainzModel? = null,

    @SerialName("area") val area: AreaMusicBrainzModel? = null,
    @SerialName("relations") val relations: List<RelationMusicBrainzModel>? = null,
) : MusicBrainzModel(), Place
