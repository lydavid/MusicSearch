package ly.david.data.room.history

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import ly.david.data.network.MusicBrainzResource

/**
 * Record of which entity screen a user has visited.
 * We can use this to let them deeplink back to this screen.
 */
@Entity(tableName = "lookup_history")
data class LookupHistoryRoomModel(
    @PrimaryKey @ColumnInfo(name = "mbid") val id: String,
    @ColumnInfo(name = "title") val title: String = "",
    @ColumnInfo(name = "resource") val resource: MusicBrainzResource, // TODO: rename
    @ColumnInfo(name = "number_of_visits") val numberOfVisits: Int = 1,
    @ColumnInfo(name = "last_accessed") val lastAccessed: Date = Date(),
    @ColumnInfo(name = "search_hint") val searchHint: String = "",
    @ColumnInfo(name = "deleted", defaultValue = "false") val deleted: Boolean = false,
)
