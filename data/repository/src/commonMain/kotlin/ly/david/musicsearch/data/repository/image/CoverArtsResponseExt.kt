package ly.david.musicsearch.data.repository.image

import kotlinx.collections.immutable.toPersistentList
import ly.david.musicsearch.data.coverart.api.CoverArtUrls
import ly.david.musicsearch.data.coverart.api.CoverArtsResponse
import ly.david.musicsearch.shared.domain.image.ImageMetadata

internal fun CoverArtsResponse.toImageMetadataList(): List<ImageMetadata> {
    return coverArtUrls.map { it.toImageMetadata() }
}

private fun CoverArtUrls.toImageMetadata(): ImageMetadata {
    return ImageMetadata(
        thumbnailUrl = getThumbnailUrl().orEmpty(),
        largeUrl = getUrl().orEmpty(),
        types = types.toPersistentList(),
        comment = comment,
    )
}

private fun CoverArtUrls.getThumbnailUrl(): String? {
    return thumbnailsUrls?.resolution250Url ?: thumbnailsUrls?.small ?: thumbnailsUrls?.resolution500Url
        ?: thumbnailsUrls?.large ?: thumbnailsUrls?.resolution1200Url
}

private fun CoverArtUrls.getUrl(): String? {
    return thumbnailsUrls?.resolution1200Url ?: thumbnailsUrls?.resolution500Url ?: thumbnailsUrls?.large
        ?: thumbnailsUrls?.resolution250Url ?: thumbnailsUrls?.small
}
