package ly.david.data.domain.history

import ly.david.data.room.history.LookupHistoryDao
import org.koin.core.annotation.Single

@Single
class LookupHistoryRepository(
    private val lookupHistoryDao: LookupHistoryDao,
) {
    fun getAllLookupHistory(query: String, sortOption: HistorySortOption) =
        lookupHistoryDao.getAllLookupHistory(
            query = "%$query%",
            alphabetically = sortOption == HistorySortOption.ALPHABETICALLY,
            alphabeticallyReverse = sortOption == HistorySortOption.ALPHABETICALLY_REVERSE,
            recentlyVisited = sortOption == HistorySortOption.RECENTLY_VISITED,
            leastRecentlyVisited = sortOption == HistorySortOption.LEAST_RECENTLY_VISITED,
            mostVisited = sortOption == HistorySortOption.MOST_VISITED,
            leastVisited = sortOption == HistorySortOption.LEAST_VISITED,
        )

    suspend fun markHistoryAsDeleted(mbid: String) {
        lookupHistoryDao.markAsDeleted(mbid, true)
    }

    suspend fun undoDeleteHistory(mbid: String) {
        lookupHistoryDao.markAsDeleted(mbid, false)
    }

    suspend fun markAllHistoryAsDeleted() {
        lookupHistoryDao.markAllAsDeleted(true)
    }

    suspend fun undoDeleteAllHistory() {
        lookupHistoryDao.markAllAsDeleted(false)
    }

    suspend fun deleteHistory(mbid: String) {
        lookupHistoryDao.delete(mbid)
    }

    suspend fun deleteAllHistory() {
        lookupHistoryDao.deleteAll()
    }
}
