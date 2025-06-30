package ly.david.musicsearch.shared.domain.listitem

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ly.david.musicsearch.shared.domain.history.NowPlayingHistory

data class NowPlayingHistoryListItemModel(
    override val id: String,
    val title: String,
    val artist: String,
    val lastPlayed: Instant = Clock.System.now(),
) : ListItemModel

private const val TITLE_ARTIST_DELIMITER = "by"

fun NowPlayingHistory.toNowPlayingHistoryListItemModel(): NowPlayingHistoryListItemModel {
    // This does not account for any "by" in the artist or track title
    val substrings = raw.split(TITLE_ARTIST_DELIMITER)
    val title = substrings.firstOrNull().orEmpty().trim()
    val artist = substrings.lastOrNull().orEmpty().trim()

    return NowPlayingHistoryListItemModel(
        id = raw,
        title = title,
        artist = artist,
        lastPlayed = lastPlayed,
    )
}
