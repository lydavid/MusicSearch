package ly.david.data.coverart

/**
 * Builds a fully qualified cover art url from [coverArtPath].
 *
 * @param thumbnail Determines whether to build url for smaller thumbnail image or not.
 */
fun buildCoverArtUrl(
    coverArtPath: String,
    thumbnail: Boolean = true
): String {
    return if (coverArtPath.isEmpty()) {
        ""
    } else {
        coverArtPath + if (thumbnail) SMALL_SUFFIX else LARGE_SUFFIX
    }
}
