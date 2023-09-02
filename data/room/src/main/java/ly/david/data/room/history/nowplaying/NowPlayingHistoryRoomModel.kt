package ly.david.data.room.history.nowplaying

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import ly.david.data.core.Identifiable

/**
 * Records notifications from Pixel's Now Playing.
 *
 * @param raw The raw notification title from Now Playing that contains both the title, delimiter, and artist name.
 *   Because we will be performing a naive split on just "by" to extract the title and artist, we will store
 *   the whole thing so that we can improve it at a later date.
 * @param lastPlayed When this song was last played.
 */
@Entity(
    tableName = "now_playing_history",
)
data class NowPlayingHistoryRoomModel(
    @PrimaryKey @ColumnInfo(name = "raw") val raw: String,
    @ColumnInfo(name = "last_played") val lastPlayed: Date = Date(),
) : Identifiable {
    override val id: String
        get() = raw
}
