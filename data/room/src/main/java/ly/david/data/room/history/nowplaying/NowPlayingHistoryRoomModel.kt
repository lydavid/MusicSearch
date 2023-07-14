package ly.david.data.room.history.nowplaying

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.util.Date
import ly.david.data.Identifiable

@Entity(
    tableName = "now_playing_history",
    primaryKeys = ["title", "text"],
)
data class NowPlayingHistoryRoomModel(
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "last_played") val lastPlayed: Date = Date(),
) : Identifiable {
    override val id: String
        get() = "${title}_$text"
}
