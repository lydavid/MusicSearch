package ly.david.data.network.api.coverart

// TODO: for now, we will use request for large thumbnail instead of full-size since some of them might be MBs
//  might be better user experience to allow them to choose what resolution to download, otherwise just show 500
private const val SMALL_SUFFIX = "-250"
private const val LARGE_SUFFIX = "-500"

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
        "${COVER_ART_ARCHIVE_BASE_URL}release/$coverArtPath" + if (thumbnail) SMALL_SUFFIX else LARGE_SUFFIX
    }
}

fun String.trimCoverArtSuffix(): String {
    return this.replace(SMALL_SUFFIX, "").replace(LARGE_SUFFIX, "")
}
