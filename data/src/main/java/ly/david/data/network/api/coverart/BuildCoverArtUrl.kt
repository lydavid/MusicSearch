package ly.david.data.network.api.coverart

fun buildReleaseCoverArtUrl(releaseId: String, coverArtPath: String): String {
    return if (coverArtPath.isEmpty()) {
        ""
    } else {
        "${COVER_ART_ARCHIVE_BASE_URL}release/$releaseId/$coverArtPath"
    }
}

// TODO: Release group cover arts also uses /release, but the id is the release id rather than release group id.
//  so it will need to save release id in the path as well
