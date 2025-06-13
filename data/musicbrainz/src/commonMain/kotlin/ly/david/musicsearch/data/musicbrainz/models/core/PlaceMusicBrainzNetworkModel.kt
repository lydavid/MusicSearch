package ly.david.musicsearch.data.musicbrainz.models.core

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ly.david.musicsearch.data.musicbrainz.models.common.AliasMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.common.LifeSpanMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.relation.RelationMusicBrainzModel
import ly.david.musicsearch.shared.domain.place.Coordinates
import ly.david.musicsearch.shared.domain.place.Place

@Serializable
data class PlaceMusicBrainzNetworkModel(
    @SerialName("id") override val id: String,
    @SerialName("name") override val name: String,
    @SerialName("disambiguation") override val disambiguation: String? = null,
    @SerialName("address") override val address: String = "",
    @SerialName("type") override val type: String? = null,
    @SerialName("type-id") val typeId: String? = null,
    @SerialName("coordinates") override val coordinates: CoordinatesMusicBrainzModel? = null,
    @SerialName("life-span") override val lifeSpan: LifeSpanMusicBrainzModel? = null,

    @SerialName("area") val area: AreaMusicBrainzNetworkModel? = null,
    @SerialName("relations") val relations: List<RelationMusicBrainzModel>? = null,
    @SerialName("aliases") override val aliases: List<AliasMusicBrainzNetworkModel>? = null,
) : MusicBrainzNetworkModel, Place

@Serializable
data class CoordinatesMusicBrainzModel(
    override val longitude: Double? = null,
    override val latitude: Double? = null,
) : Coordinates
