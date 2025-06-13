package ly.david.musicsearch.data.musicbrainz.models.core

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ly.david.musicsearch.data.musicbrainz.models.MediumMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.common.AliasMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.common.ArtistCreditMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.relation.RelationMusicBrainzModel
import ly.david.musicsearch.shared.domain.NameWithDisambiguation
import ly.david.musicsearch.shared.domain.release.CoverArtArchive
import ly.david.musicsearch.shared.domain.release.Release
import ly.david.musicsearch.shared.domain.release.TextRepresentation

@Serializable
data class ReleaseMusicBrainzNetworkModel(
    @SerialName("id") override val id: String,
    @SerialName("title") override val name: String,
    @SerialName("disambiguation") override val disambiguation: String = "",
    @SerialName("date") override val date: String? = null,
    @SerialName("status") override val status: String? = null,
    @SerialName("status-id") override val statusId: String? = null,
    @SerialName("barcode") override val barcode: String? = null,
    @SerialName("country") override val countryCode: String? = null,
    @SerialName("packaging") override val packaging: String? = null,
    @SerialName("packaging-id") override val packagingId: String? = null,
    @SerialName("asin") override val asin: String? = null,
    @SerialName("quality") override val quality: String? = null,
    @SerialName("cover-art-archive")
    val coverArtArchive: CoverArtArchiveMusicBrainzModel = CoverArtArchiveMusicBrainzModel(),
    @SerialName("text-representation")
    val textRepresentation: TextRepresentationMusicBrainzModel? = null,

    @SerialName("release-events") val releaseEvents: List<ReleaseEventMusicBrainzModel>? = null,

    // Use inc=media for subqueries. inc=recordings for release lookup
    @SerialName("media") val media: List<MediumMusicBrainzModel>? = null,

    // Use inc=artist-credits for subqueries. inc=artists for release lookup
    @SerialName("artist-credit") val artistCredits: List<ArtistCreditMusicBrainzModel>? = null,

    // inc=labels
    @SerialName("label-info") val labelInfoList: List<LabelInfo>? = null,

    // inc=release-groups
    @SerialName("release-group") val releaseGroup: ReleaseGroupMusicBrainzNetworkModel? = null,

    @SerialName("relations") val relations: List<RelationMusicBrainzModel>? = null,
    @SerialName("aliases") override val aliases: List<AliasMusicBrainzNetworkModel>? = null,
) : MusicBrainzNetworkModel, Release

@Serializable
data class CoverArtArchiveMusicBrainzModel(
    //    @SerialName("darkened") val darkened: Boolean = false,
//    @SerialName("artwork") val artwork: Boolean = false,
//    @SerialName("back") val back: Boolean = false,
//    @SerialName("front") val front: Boolean = false,
    override val count: Int = 0,
) : CoverArtArchive

@Serializable
data class TextRepresentationMusicBrainzModel(
    override val script: String? = null,
    override val language: String? = null,
) : TextRepresentation

@Serializable
data class ReleaseEventMusicBrainzModel(
    @SerialName("area") val area: AreaMusicBrainzNetworkModel? = null,
    @SerialName("date") val date: String? = null,
    @SerialName("type") val type: String? = null,
    @SerialName("type-id") val typeId: String? = null,
    @SerialName("sort-name") val sortName: String? = null,
    @SerialName("name") override val name: String? = null,
    @SerialName("disambiguation") override val disambiguation: String? = null,
) : NameWithDisambiguation

@Serializable
data class LabelInfo(
    @SerialName("catalog-number") val catalogNumber: String? = null,
    @SerialName("label") val label: LabelMusicBrainzNetworkModel? = null,
)
