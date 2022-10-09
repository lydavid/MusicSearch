package ly.david.mbjc.data.persistence.history

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import java.util.Date
import ly.david.mbjc.data.persistence.BaseDao

@Dao
internal abstract class LookupHistoryDao : BaseDao<LookupHistory> {

    @Transaction
    @Query("SELECT * FROM lookup_history ORDER BY last_accessed DESC")
    abstract fun getAllLookupHistory(): PagingSource<Int, LookupHistory>

    // TODO: can't search "release group", need to use "release_group" or "release-group"
    //  rather than having the user type "artist" to filter artist, use pills or something
    //  reserving text search for title only
    @Transaction
    @Query(
        """
        SELECT * 
        FROM lookup_history
        WHERE title LIKE :query OR resource LIKE :query
        ORDER BY last_accessed DESC
        """
    )
    abstract fun getAllLookupHistoryFiltered(query: String): PagingSource<Int, LookupHistory>

    @Query(
        """
        UPDATE lookup_history 
        SET number_of_visits = number_of_visits + 1,
            last_accessed = :lastAccessed
        WHERE mbid = :mbid
        """
    )
    abstract suspend fun incrementVisitAndDateAccessed(mbid: String, lastAccessed: Date = Date()): Int

    /**
     * Insert new [LookupHistory] if it doesn't exist, otherwise increment its visited count
     * and its last visited timestamp.
     */
    suspend fun incrementOrInsertLookupHistory(lookupHistory: LookupHistory) {
        val numUpdated = incrementVisitAndDateAccessed(lookupHistory.mbid)
        if (numUpdated == 0) {
            insert(lookupHistory)
        }
    }
}
