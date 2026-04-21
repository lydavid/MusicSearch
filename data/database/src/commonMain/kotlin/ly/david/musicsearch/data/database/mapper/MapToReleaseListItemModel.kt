package ly.david.musicsearch.data.database.mapper

import ly.david.musicsearch.data.database.UNKNOWN_LISTENS_FLAG
import ly.david.musicsearch.shared.domain.image.ImageSource
import ly.david.musicsearch.shared.domain.listitem.ReleaseListItemModel
import ly.david.musicsearch.shared.domain.release.ReleaseStatus
import ly.david.musicsearch.shared.domain.release.TextRepresentationUiModel

internal fun mapToReleaseListItemModel(
    id: String,
    name: String,
    disambiguation: String,
    date: String,
    barcode: String,
    asin: String,
    quality: String,
    countryCode: String,
    status: ReleaseStatus,
    packaging: String,
    packagingId: String,
    script: String,
    language: String,
    formattedArtistCreditNames: String?,
    imageId: Long?,
    source: ImageSource?,
    thumbnailUrl: String?,
    releaseCountryCount: Long,
    visited: Boolean,
    collected: Boolean,
    aliasNames: String?,
    aliasLocales: String?,
    listenCount: Long?,
    completeListenCount: Long,
) = ReleaseListItemModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
    date = date,
    barcode = barcode,
    status = status,
    countryCode = countryCode,
    packaging = packaging,
    packagingId = packagingId,
    asin = asin,
    quality = quality,
    textRepresentation = TextRepresentationUiModel(
        script = script,
        language = language,
    ),
    imageMetadata = mapToImageMetadata(
        id = imageId,
        thumbnailUrl = thumbnailUrl,
        source = source,
    ),
    formattedArtistCredits = formattedArtistCreditNames,
    releaseCountryCount = releaseCountryCount.toInt(),
    visited = visited,
    collected = collected,
    aliases = combineToAliases(aliasNames, aliasLocales),
    listenState = toListenState(
        listenCount = listenCount,
        completeListenCount = completeListenCount,
    ),
)

/**
 * With [catalogNumber].
 */
internal fun mapToReleaseListItemModel(
    id: String,
    name: String,
    disambiguation: String,
    date: String,
    barcode: String,
    asin: String,
    quality: String,
    countryCode: String,
    status: ReleaseStatus,
    packaging: String,
    packagingId: String,
    script: String,
    language: String,
    formattedArtistCreditNames: String?,
    imageId: Long?,
    source: ImageSource?,
    thumbnailUrl: String?,
    releaseCountryCount: Long,
    visited: Boolean,
    collected: Boolean,
    aliasNames: String?,
    aliasLocales: String?,
    listenCount: Long?,
    completeListenCount: Long,
    catalogNumber: String?,
) = ReleaseListItemModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
    date = date,
    barcode = barcode,
    status = status,
    countryCode = countryCode,
    packaging = packaging,
    packagingId = packagingId,
    asin = asin,
    quality = quality,
    catalogNumbers = catalogNumber,
    textRepresentation = TextRepresentationUiModel(
        script = script,
        language = language,
    ),
    imageMetadata = mapToImageMetadata(
        id = imageId,
        thumbnailUrl = thumbnailUrl,
        source = source,
    ),
    formattedArtistCredits = formattedArtistCreditNames,
    releaseCountryCount = releaseCountryCount.toInt(),
    visited = visited,
    collected = collected,
    aliases = combineToAliases(aliasNames, aliasLocales),
    listenState = toListenState(
        listenCount = listenCount,
        completeListenCount = completeListenCount,
    ),
)

private fun toListenState(
    listenCount: Long?,
    completeListenCount: Long,
) = when (listenCount) {
    null -> ReleaseListItemModel.ListenState.Hide
    UNKNOWN_LISTENS_FLAG -> ReleaseListItemModel.ListenState.Unknown
    else -> ReleaseListItemModel.ListenState.Known(
        listenCount = listenCount,
        completeListenCount = completeListenCount,
    )
}
