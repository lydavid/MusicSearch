package ly.david.musicsearch.data.musicbrainz.models.core

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ly.david.musicsearch.data.musicbrainz.models.common.LifeSpanMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.relation.RelationMusicBrainzModel
import ly.david.musicsearch.shared.domain.artist.Artist

@Serializable
data class ArtistMusicBrainzNetworkModel(
    @SerialName("id") override val id: String,
    @SerialName("name") override val name: String = "",
    @SerialName("sort-name") override val sortName: String = "",
    @SerialName("disambiguation") override val disambiguation: String? = null,
    @SerialName("type") override val type: String? = null,
    @SerialName("type-id") val typeId: String? = null,
    @SerialName("gender") override val gender: String? = null,
    @SerialName("ipis") val ipis: List<String>? = null,
    @SerialName("isnis") val isnis: List<String>? = null,
    @SerialName("country") val countryCode: String? = null,
    @SerialName("life-span") val lifeSpan: LifeSpanMusicBrainzModel? = null,

    @SerialName("relations") val relations: List<RelationMusicBrainzModel>? = null,
    @SerialName("area") val area: AreaMusicBrainzNetworkModel? = null,
    @SerialName("aliases") val aliases: List<AliasMusicBrainzNetworkModel>? = null,
) : MusicBrainzNetworkModel(), Artist
