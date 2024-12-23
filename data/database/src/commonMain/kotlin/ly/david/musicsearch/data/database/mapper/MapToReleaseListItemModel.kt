package ly.david.musicsearch.data.database.mapper

import ly.david.musicsearch.shared.domain.listitem.ReleaseListItemModel
import ly.david.musicsearch.shared.domain.release.CoverArtArchiveUiModel
import ly.david.musicsearch.shared.domain.release.TextRepresentationUiModel

// Although SQLDelight generates models for us, their types are based on the function names.
internal fun mapToReleaseListItemModel(
    id: String,
    name: String,
    disambiguation: String,
    date: String?,
    barcode: String?,
    asin: String?,
    quality: String?,
    countryCode: String?,
    status: String?,
    statusId: String?,
    packaging: String?,
    packagingId: String?,
    script: String?,
    language: String?,
    coverArtCount: Int,
    formattedArtistCreditNames: String,
    thumbnailUrl: String?,
    placeholderKey: Long?,
    releaseCountryCount: Long,
    visited: Boolean?,
) = ReleaseListItemModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
    date = date,
    barcode = barcode,
    asin = asin,
    quality = quality,
    countryCode = countryCode,
    status = status,
    statusId = statusId,
    packaging = packaging,
    packagingId = packagingId,
    textRepresentation = TextRepresentationUiModel(
        script = script,
        language = language,
    ),
    coverArtArchive = CoverArtArchiveUiModel(
        count = coverArtCount,
    ),
    formattedArtistCredits = formattedArtistCreditNames,
    imageUrl = thumbnailUrl,
    imageId = placeholderKey ?: 0L,
    releaseCountryCount = releaseCountryCount.toInt(),
    visited = visited == true,
)

/**
 * With [catalogNumber].
 */
internal fun mapToReleaseListItemModel(
    id: String,
    name: String,
    disambiguation: String,
    date: String?,
    barcode: String?,
    asin: String?,
    quality: String?,
    countryCode: String?,
    status: String?,
    statusId: String?,
    packaging: String?,
    packagingId: String?,
    script: String?,
    language: String?,
    coverArtCount: Int,
    catalogNumber: String?,
    formattedArtistCreditNames: String,
    thumbnailUrl: String?,
    placeholderKey: Long?,
    releaseCountryCount: Long,
    visited: Boolean?,
) = ReleaseListItemModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
    date = date,
    barcode = barcode,
    asin = asin,
    quality = quality,
    countryCode = countryCode,
    status = status,
    statusId = statusId,
    packaging = packaging,
    packagingId = packagingId,
    textRepresentation = TextRepresentationUiModel(
        script = script,
        language = language,
    ),
    coverArtArchive = CoverArtArchiveUiModel(
        count = coverArtCount,
    ),
    catalogNumbers = catalogNumber,
    formattedArtistCredits = formattedArtistCreditNames,
    imageUrl = thumbnailUrl,
    imageId = placeholderKey ?: 0L,
    releaseCountryCount = releaseCountryCount.toInt(),
    visited = visited == true,
)
