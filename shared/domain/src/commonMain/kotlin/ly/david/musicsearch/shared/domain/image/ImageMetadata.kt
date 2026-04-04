@file:JvmName("ImageMetadataOldKt")

package ly.david.musicsearch.shared.domain.image

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.shared.domain.common.prependHttpsIfMissing
import ly.david.musicsearch.shared.domain.common.transformThisIfNotNullOrEmpty
import kotlin.jvm.JvmName

private const val WIKIMEDIA_IMAGE_BASE_URL = "https://commons.wikimedia.org/w/index.php?title=Special:Redirect/file/"
private const val WIKIMEDIA_LINK_BASE_URL = "https://commons.wikimedia.org/wiki/File:"

sealed interface ImageMetadata {
    val imageId: ImageId

    val thumbnailImageUrl: String
    val largeImageUrl: String
    val linkUrl: String

    data class InternetArchive(
        override val imageId: ImageId = ImageId(0L),
        private val rawThumbnailUrl: String = "",
        private val rawLargeUrl: String = "",
        val types: ImmutableList<String> = persistentListOf(),
        val comment: String = "",
    ) : ImageMetadata {
        override val thumbnailImageUrl: String
            get() {
                return rawThumbnailUrl.transformThisIfNotNullOrEmpty { it.prependHttpsIfMissing() }
            }
        override val largeImageUrl: String
            get() {
                return rawLargeUrl.transformThisIfNotNullOrEmpty { it.prependHttpsIfMissing() }
            }
        override val linkUrl: String
            get() {
                return rawLargeUrl.transformThisIfNotNullOrEmpty { it.prependHttpsIfMissing() }
            }
    }

    data class Spotify(
        override val imageId: ImageId = ImageId(0L),
        private val rawThumbnailUrl: String = "",
        private val rawLargeUrl: String = "",
    ) : ImageMetadata {
        override val thumbnailImageUrl: String
            get() {
                return rawThumbnailUrl.transformThisIfNotNullOrEmpty { it.prependHttpsIfMissing() }
            }
        override val largeImageUrl: String
            get() {
                return rawLargeUrl.transformThisIfNotNullOrEmpty { it.prependHttpsIfMissing() }
            }
        override val linkUrl: String
            get() {
                return rawLargeUrl.transformThisIfNotNullOrEmpty { it.prependHttpsIfMissing() }
            }
    }

    data class Wikimedia(
        override val imageId: ImageId = ImageId(0L),
        private val rawThumbnailUrl: String = "",
        private val rawLargeUrl: String = "",
    ) : ImageMetadata {
        override val thumbnailImageUrl: String
            get() {
                return rawThumbnailUrl.transformThisIfNotNullOrEmpty { "${WIKIMEDIA_IMAGE_BASE_URL}$it" }
            }
        override val largeImageUrl: String
            get() {
                return rawLargeUrl.transformThisIfNotNullOrEmpty { "${WIKIMEDIA_IMAGE_BASE_URL}$it" }
            }
        override val linkUrl: String
            get() {
                return rawLargeUrl.transformThisIfNotNullOrEmpty { "${WIKIMEDIA_LINK_BASE_URL}$it" }
            }
    }
}
