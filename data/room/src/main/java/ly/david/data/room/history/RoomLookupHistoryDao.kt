package ly.david.data.room.history

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.room.BaseDao

@Dao
abstract class RoomLookupHistoryDao : BaseDao<LookupHistoryRoomModel>() {

    @Transaction
    @Query(
        """
        SELECT * 
        FROM lookup_history
        WHERE NOT deleted AND
          (title LIKE :query OR resource LIKE :query OR search_hint LIKE :query)
        ORDER BY
          CASE WHEN :alphabetically THEN title END ASC,
          CASE WHEN :alphabeticallyReverse THEN title END DESC,
          CASE WHEN :recentlyVisited THEN last_accessed END DESC,
          CASE WHEN :leastRecentlyVisited THEN last_accessed END ASC,
          CASE WHEN :mostVisited THEN number_of_visits END DESC,
          CASE WHEN :leastVisited THEN number_of_visits END ASC
        """
    )
    abstract fun getAllLookupHistory(
        query: String,
        alphabetically: Boolean,
        alphabeticallyReverse: Boolean,
        recentlyVisited: Boolean,
        leastRecentlyVisited: Boolean,
        mostVisited: Boolean,
        leastVisited: Boolean,
    ): PagingSource<Int, RoomLookupHistoryForListItem>

//    @Query(
//        """
//        SELECT *
//        FROM lookup_history
//        WHERE mbid = :mbid
//    """
//    )
//    abstract suspend fun getLookupHistory(mbid: String): LookupHistoryRoomModel?

    @Query(
        """
            UPDATE lookup_history
            SET deleted = :deleted
            WHERE mbid = :mbid
        """
    )
    abstract suspend fun markAsDeleted(mbid: String, deleted: Boolean)

    @Query(
        """
            UPDATE lookup_history
            SET deleted = :deleted
        """
    )
    abstract suspend fun markAllAsDeleted(deleted: Boolean)

    @Query(
        """
            DELETE FROM lookup_history
            WHERE mbid = :mbid
        """
    )
    abstract suspend fun delete(mbid: String)

    @Query(
        """
            DELETE FROM lookup_history
        """
    )
    abstract suspend fun deleteAll()

    /**
     * Insert new [LookupHistoryRoomModel] if it doesn't exist, otherwise increment its visited count
     * and its last visited timestamp.
     */
//    suspend fun incrementOrInsertLookupHistory(lookupHistory: LookupHistoryRoomModel) {
//        val historyRecord = getLookupHistory(lookupHistory.id)
//        if (historyRecord == null) {
//            insert(lookupHistory)
//        } else {
//            upsert(
//                historyRecord.copy(
//                    title = lookupHistory.title,
//                    numberOfVisits = historyRecord.numberOfVisits + 1,
//                    lastAccessed = Date(),
//                    searchHint = lookupHistory.searchHint
//                )
//            )
//        }
//    }
}
