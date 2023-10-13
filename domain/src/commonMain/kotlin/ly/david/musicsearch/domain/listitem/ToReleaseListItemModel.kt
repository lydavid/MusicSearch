package ly.david.musicsearch.domain.listitem

import ly.david.data.musicbrainz.ReleaseMusicBrainzModel
import ly.david.musicsearch.data.core.artist.getDisplayNames
import ly.david.musicsearch.data.core.listitem.ReleaseListItemModel
import ly.david.musicsearch.domain.release.toCoverArtArchiveUiModel
import ly.david.musicsearch.domain.release.toTextRepresentationUiModel

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
    formattedArtistCredits = artistCredits.getDisplayNames(),
    releaseCountryCount = releaseEvents?.count() ?: 0,
)
