package ly.david.data.domain.listitem

import java.util.Date
import ly.david.data.room.history.nowplaying.NowPlayingHistoryRoomModel

data class NowPlayingHistoryListItemModel(
    override val id: String,
    val title: String,
    val artist: String,
    val lastPlayed: Date = Date(),
) : ListItemModel()

private const val TITLE_ARTIST_DELIMITER = "by"

fun NowPlayingHistoryRoomModel.toNowPlayingHistoryListItemModel(): NowPlayingHistoryListItemModel {
    // This does not account for any "by" in the artist or track title
    val substrings = raw.split(TITLE_ARTIST_DELIMITER)
    val title = substrings.firstOrNull().orEmpty().trim()
    val artist = substrings.lastOrNull().orEmpty().trim()

    return NowPlayingHistoryListItemModel(
        id = id,
        title = title,
        artist = artist,
        lastPlayed = lastPlayed,
    )
}
