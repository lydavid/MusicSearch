package ly.david.musicsearch.data.database.mapper

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.shared.domain.image.ImageId
import ly.david.musicsearch.shared.domain.image.ImageMetadata
import ly.david.musicsearch.shared.domain.image.ImageSource

fun mapToImageMetadata(
    id: Long?,
    source: ImageSource?,
    thumbnailUrl: String?,
): ImageMetadata? {
    if (id == null || source == null || thumbnailUrl == null) return null
    return mapToImageMetadata(
        id = id,
        source = source,
        thumbnailUrl = thumbnailUrl,
        largeUrl = "",
        types = persistentListOf(),
        comment = "",
    )
}

fun mapToImageMetadata(
    id: Long,
    source: ImageSource,
    thumbnailUrl: String,
    largeUrl: String,
    types: ImmutableList<String>?,
    comment: String?,
): ImageMetadata {
    return when (source) {
        ImageSource.INTERNET_ARCHIVE -> {
            ImageMetadata.InternetArchive(
                imageId = ImageId(id),
                rawThumbnailUrl = thumbnailUrl,
                rawLargeUrl = largeUrl,
                types = types ?: persistentListOf(),
                comment = comment.orEmpty(),
            )
        }

        ImageSource.SPOTIFY -> {
            ImageMetadata.Spotify(
                imageId = ImageId(id),
                rawThumbnailUrl = thumbnailUrl,
                rawLargeUrl = largeUrl,
            )
        }

        ImageSource.WIKIMEDIA -> {
            ImageMetadata.Wikimedia(
                imageId = ImageId(id),
                rawThumbnailUrl = thumbnailUrl,
                rawLargeUrl = largeUrl,
            )
        }
    }
}
