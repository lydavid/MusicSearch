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
        SELECT * 
        FROM lookup_history
        WHERE mbid = :mbid
    """
    )
    abstract suspend fun getLookupHistory(mbid: String): LookupHistory?

    @Query(
        """
            DELETE FROM lookup_history
        """
    )
    abstract suspend fun deleteAllHistory()

    /**
     * Insert new [LookupHistory] if it doesn't exist, otherwise increment its visited count
     * and its last visited timestamp.
     */
    suspend fun incrementOrInsertLookupHistory(lookupHistory: LookupHistory) {
        val historyRecord = getLookupHistory(lookupHistory.mbid)
        if (historyRecord == null) {
            insert(lookupHistory)
        } else if (historyRecord.title.isEmpty()) {
            insert(
                historyRecord.copy(
                    title = lookupHistory.title,
                    numberOfVisits = historyRecord.numberOfVisits + 1,
                    lastAccessed = Date()
                )
            )
        } else {
            insert(
                historyRecord.copy(
                    numberOfVisits = historyRecord.numberOfVisits + 1,
                    lastAccessed = Date()
                )
            )
        }
    }
}
