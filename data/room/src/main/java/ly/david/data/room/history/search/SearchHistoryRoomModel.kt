package ly.david.data.room.history.search

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.util.Date
import ly.david.data.Identifiable
import ly.david.data.network.MusicBrainzEntity

/**
 * Record of what the user has recently searched.
 * Allows the user to deeplink search with the same query.
 *
 * @param query Search query.
 * @param entity Which entity we were searching.
 * @param lastAccessed When we searched for it.
 */
@Entity(
    tableName = "search_history",
    primaryKeys = ["query", "entity"],
)
data class SearchHistoryRoomModel(
    @ColumnInfo(name = "query") val query: String,
    @ColumnInfo(name = "entity") val entity: MusicBrainzEntity,
    @ColumnInfo(name = "last_accessed") val lastAccessed: Date = Date()
) : Identifiable {
    override val id: String
        get() = "${query}_$entity"
}
