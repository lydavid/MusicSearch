package ly.david.data.musicbrainz

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ly.david.data.core.NameWithDisambiguation
import ly.david.data.core.release.Release

@Serializable
data class ReleaseMusicBrainzModel(
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
    @SerialName("release-group") val releaseGroup: ReleaseGroupMusicBrainzModel? = null,

    @SerialName("relations") val relations: List<RelationMusicBrainzModel>? = null,
) : MusicBrainzModel(), Release

@Serializable
data class ReleaseEventMusicBrainzModel(
    @SerialName("area") val area: AreaMusicBrainzModel? = null,
    @SerialName("date") val date: String? = null,
    @SerialName("type") val type: String? = null,
    @SerialName("type-id") val typeId: String? = null,
    @SerialName("sort-name") val sortName: String? = null,
    @SerialName("name") override val name: String? = null,
    @SerialName("disambiguation") override val disambiguation: String? = null,
) : NameWithDisambiguation
