package ly.david.musicsearch.data.core

/**
 * The number of tracks in each media in the list, in sequential order.
 *
 * Example returns:
 * * 23
 * * 15 + 8 + 24
 */
fun List<Int>?.getTracksForDisplay(): String {
    val tracksForDisplay = this?.joinToString(" + ") {
        "$it"
    }
    return if (tracksForDisplay.isNullOrEmpty()) {
        ""
    } else {
        tracksForDisplay
    }
}
