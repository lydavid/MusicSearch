package ly.david.data.room.history.search

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.util.Date
import ly.david.data.Identifiable
import ly.david.data.network.MusicBrainzResource

/**
 * Record of what the user has recently searched.
 * Allows the user to deeplink search with the same query.
 *
 * @param id Used just to conform to [Identifiable].
 *  Just combine the primary keys [query] and [entity].
 * @param query Search query.
 * @param entity Which entity we were searching.
 * @param lastAccessed When we searched for it.
 */
@Entity(
    tableName = "search_history",
    primaryKeys = ["query", "entity"],
)
data class SearchHistoryRoomModel(
    @ColumnInfo(name = "id") override val id: String,
    @ColumnInfo(name = "query") val query: String,
    @ColumnInfo(name = "entity") val entity: MusicBrainzResource,
    @ColumnInfo(name = "last_accessed") val lastAccessed: Date = Date()
) : Identifiable
