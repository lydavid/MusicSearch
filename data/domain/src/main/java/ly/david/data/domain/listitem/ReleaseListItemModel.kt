package ly.david.data.domain.listitem

import ly.david.data.core.Release
import ly.david.data.core.ReleaseForListItem
import ly.david.data.core.getDisplayNames
import ly.david.data.domain.release.CoverArtArchiveUiModel
import ly.david.data.domain.release.TextRepresentationUiModel
import ly.david.data.domain.release.toCoverArtArchiveUiModel
import ly.david.data.domain.release.toTextRepresentationUiModel
import ly.david.data.musicbrainz.ReleaseMusicBrainzModel
import ly.david.data.room.area.releases.ReleaseCountry

data class ReleaseListItemModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String = "",
    override val date: String? = null,
    override val barcode: String? = null,
    override val status: String? = null,
    override val statusId: String? = null,
    override val countryCode: String? = null,
    override val packaging: String? = null,
    override val packagingId: String? = null,
    override val asin: String? = null,
    override val quality: String? = null,

    val coverArtArchive: CoverArtArchiveUiModel = CoverArtArchiveUiModel(),
    val textRepresentation: TextRepresentationUiModel? = null,
    val imageUrl: String? = null,

    val formattedFormats: String? = null,
    val formattedTracks: String? = null,
    val formattedArtistCredits: String? = null,

    val releaseCountries: List<ReleaseCountry> = listOf(),
) : ListItemModel(), Release

fun ReleaseMusicBrainzModel.toReleaseListItemModel() = ReleaseListItemModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
    date = date,
    status = status,
    barcode = barcode,
    statusId = statusId,
    countryCode = countryCode,
    packaging = packaging,
    packagingId = packagingId,
    coverArtArchive = coverArtArchive.toCoverArtArchiveUiModel(),
    textRepresentation = textRepresentation?.toTextRepresentationUiModel(),
    asin = asin,
    quality = quality,
    imageUrl = null,
    formattedArtistCredits = artistCredits.getDisplayNames()
)

fun ReleaseForListItem.toReleaseListItemModel() = ReleaseListItemModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
    date = date,
    status = status,
    barcode = barcode,
    statusId = statusId,
    countryCode = countryCode,
    packaging = packaging,
    packagingId = packagingId,
    asin = asin,
    quality = quality,
    coverArtArchive = CoverArtArchiveUiModel(
        count = coverArtCount,
    ),
    textRepresentation = TextRepresentationUiModel(
        script = script,
        language = language,
    ),
//    formattedFormats = formatTrackCounts.map { it.format }.getFormatsForDisplay(),
//    formattedTracks = formatTrackCounts.map { it.trackCount }.getTracksForDisplay(),
    imageUrl = thumbnailUrl,
    formattedArtistCredits = formattedArtistCreditNames,
//    releaseCountries = releaseCountries,
)
