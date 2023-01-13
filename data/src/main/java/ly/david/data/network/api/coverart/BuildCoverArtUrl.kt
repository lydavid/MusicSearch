package ly.david.data.network.api.coverart

fun buildReleaseCoverArtUrl(releaseId: String, coverArtPath: String): String {
    return if (coverArtPath.isEmpty()) {
        ""
    } else {
        "${COVER_ART_ARCHIVE_BASE_URL}release/$releaseId/$coverArtPath"
    }
}

/**
 * Release group cover art actually comes from a release.
 * Since multiple releases can belong to a release group, we just store the path including the release id.
 */
fun buildReleaseGroupCoverArtUrl(coverArtPath: String): String {
    return if (coverArtPath.isEmpty()) {
        ""
    } else {
        "${COVER_ART_ARCHIVE_BASE_URL}release/$coverArtPath"
    }
}
