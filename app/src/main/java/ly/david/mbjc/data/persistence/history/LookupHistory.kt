package ly.david.mbjc.data.persistence.history

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date
import ly.david.mbjc.data.network.MusicBrainzResource

// TODO: We can probably make this stable by mapping it to a ui model
//  but is that worth it?
/**
 * Record of which Lookup screen a user has visited.
 * We can use this to let them deeplink back to this screen.
 */
@Entity(
    tableName = "lookup_history",
    indices = [Index(value = ["mbid"], unique = true)]
)
internal data class LookupHistory(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    @ColumnInfo(name = "summary")
    val summary: String = "",

    @ColumnInfo(name = "resource")
    val resource: MusicBrainzResource,

    // MusicBrainz id
    @ColumnInfo(name = "mbid")
    val mbid: String,

    @ColumnInfo(name = "number_of_visits")
    val numberOfVisits: Int = 1,

    @ColumnInfo(name = "last_accessed")
    val lastAccessed: Date = Date()
)
