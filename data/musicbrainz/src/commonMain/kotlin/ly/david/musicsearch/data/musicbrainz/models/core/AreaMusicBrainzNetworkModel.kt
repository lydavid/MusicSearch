package ly.david.musicsearch.data.musicbrainz.models.core

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ly.david.musicsearch.data.musicbrainz.models.common.AliasMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.common.LifeSpanMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.relation.RelationMusicBrainzModel
import ly.david.musicsearch.shared.domain.area.Area

@Serializable
data class AreaMusicBrainzNetworkModel(
    @SerialName("id") override val id: String,
    @SerialName("name") override val name: String,
    @SerialName("sort-name") override val sortName: String = "",
    @SerialName("disambiguation") override val disambiguation: String? = null,
    @SerialName("type") override val type: String? = null,
    @SerialName("type-id") val typeId: String? = null,
    @SerialName("life-span") override val lifeSpan: LifeSpanMusicBrainzModel? = null,

    @SerialName("iso-3166-1-codes") val countryCodes: List<String>? = null,
    @SerialName("iso-3166-2-codes") val countrySubDivisionCodes: List<String>? = null,
    @SerialName("iso-3166-3-codes") val formerCountryCodes: List<String>? = null,
    @SerialName("relations") val relations: List<RelationMusicBrainzModel>? = null,
    @SerialName("aliases") override val aliases: List<AliasMusicBrainzNetworkModel>? = null,
) : MusicBrainzNetworkModel, Area
