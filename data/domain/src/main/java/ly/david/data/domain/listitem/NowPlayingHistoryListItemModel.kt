package ly.david.data.domain.listitem

import java.util.Date
import ly.david.data.room.history.nowplaying.NowPlayingHistoryRoomModel

data class NowPlayingHistoryListItemModel(
    override val id: String,
    val title: String,
    val text: String,
    val lastPlayed: Date = Date()
) : ListItemModel()

fun NowPlayingHistoryRoomModel.toNowPlayingHistoryListItemModel() = NowPlayingHistoryListItemModel(
    id = id,
    title = title,
    text = text,
    lastPlayed = lastPlayed
)
