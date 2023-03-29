package ly.david.data.persistence.history

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import ly.david.data.Identifiable
import ly.david.data.network.MusicBrainzResource

// TODO: We can probably make this stable by mapping it to a ui model
/**
 * Record of which Lookup screen a user has visited.
 * We can use this to let them deeplink back to this screen.
 */
@Entity(tableName = "lookup_history")
data class LookupHistoryRoomModel(
    @PrimaryKey @ColumnInfo(name = "mbid") override val id: String,
    @ColumnInfo(name = "title") val title: String = "",
    @ColumnInfo(name = "resource") val resource: MusicBrainzResource,
    @ColumnInfo(name = "number_of_visits") val numberOfVisits: Int = 1,
    @ColumnInfo(name = "last_accessed") val lastAccessed: Date = Date(),
    @ColumnInfo(name = "search_hint") val searchHint: String = ""
) : Identifiable
