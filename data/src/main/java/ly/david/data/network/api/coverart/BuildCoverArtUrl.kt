package ly.david.data.network.api.coverart

// TODO: for now, we will use request for large thumbnail instead of full-size since some of them might be MBs
//  might be better user experience to allow them to choose what resolution to download, otherwise just show 500
private const val SMALL_SUFFIX = "-250"
private const val LARGE_SUFFIX = "-500"

fun buildReleaseCoverArtUrl(
    releaseId: String,
    coverArtPath: String,
    thumbnail: Boolean = true
): String {
    return if (coverArtPath.isEmpty()) {
        ""
    } else {
        "${COVER_ART_ARCHIVE_BASE_URL}release/$releaseId/$coverArtPath" + if (thumbnail) SMALL_SUFFIX else LARGE_SUFFIX
    }
}

/**
 * Release group cover art actually comes from a release.
 * Since multiple releases can belong to a release group, we just store the path including the release id.
 */
fun buildReleaseGroupCoverArtUrl(
    coverArtPath: String,
    thumbnail: Boolean = true
): String {
    return if (coverArtPath.isEmpty()) {
        ""
    } else {
        "${COVER_ART_ARCHIVE_BASE_URL}release/$coverArtPath" + if (thumbnail) SMALL_SUFFIX else LARGE_SUFFIX
    }
}
