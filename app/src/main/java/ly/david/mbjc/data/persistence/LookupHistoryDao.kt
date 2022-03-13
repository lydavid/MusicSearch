package ly.david.mbjc.data.persistence

import androidx.room.Dao
import androidx.room.Query
import java.util.Date

@Dao
abstract class LookupHistoryDao : BaseDao<LookupHistory> {

    @Query("SELECT * FROM lookup_history ORDER BY last_accessed DESC")
    abstract suspend fun getAllLookupHistory(): List<LookupHistory>

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
