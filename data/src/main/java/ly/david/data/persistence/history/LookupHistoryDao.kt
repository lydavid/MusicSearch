package ly.david.data.persistence.history

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import java.util.Date
import ly.david.data.persistence.BaseDao

@Dao
abstract class LookupHistoryDao : BaseDao<LookupHistoryRoomModel>() {

    @Transaction
    @Query("SELECT * FROM lookup_history ORDER BY last_accessed DESC")
    abstract fun getAllLookupHistory(): PagingSource<Int, LookupHistoryRoomModel>

    // TODO: can't search "release group", need to use "release_group" or "release-group"
    //  rather than having the user type "artist" to filter artist, use pills or something
    //  reserving text search for title only
    @Transaction
    @Query(
        """
        SELECT * 
        FROM lookup_history
        WHERE title LIKE :query OR resource LIKE :query OR search_hint LIKE :query
        ORDER BY last_accessed DESC
        """
    )
    abstract fun getAllLookupHistoryFiltered(query: String): PagingSource<Int, LookupHistoryRoomModel>

    @Query(
        """
        SELECT * 
        FROM lookup_history
        WHERE mbid = :mbid
    """
    )
    abstract suspend fun getLookupHistory(mbid: String): LookupHistoryRoomModel?

    @Query(
        """
            DELETE FROM lookup_history
        """
    )
    abstract suspend fun deleteAllHistory()

    /**
     * Insert new [LookupHistoryRoomModel] if it doesn't exist, otherwise increment its visited count
     * and its last visited timestamp.
     */
    suspend fun incrementOrInsertLookupHistory(lookupHistory: LookupHistoryRoomModel) {
        val historyRecord = getLookupHistory(lookupHistory.mbid)
        if (historyRecord == null) {
            insert(lookupHistory)
        } else {
            upsert(
                historyRecord.copy(
                    title = lookupHistory.title,
                    numberOfVisits = historyRecord.numberOfVisits + 1,
                    lastAccessed = Date(),
                    searchHint = lookupHistory.searchHint
                )
            )
        }
    }
}
