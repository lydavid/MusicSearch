package ly.david.data.domain.history

import ly.david.data.core.history.LookupHistory
import ly.david.musicsearch.data.database.dao.LookupHistoryDao
import org.koin.core.annotation.Single

@Single
class LookupHistoryRepository(
    private val lookupHistoryDao: LookupHistoryDao,
) {
    fun upsert(lookupHistory: LookupHistory) {
        lookupHistoryDao.upsert(lookupHistory)
    }

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

    fun markHistoryAsDeleted(mbid: String) {
        lookupHistoryDao.markAsDeleted(mbid, true)
    }

    fun undoDeleteHistory(mbid: String) {
        lookupHistoryDao.markAsDeleted(mbid, false)
    }

    fun markAllHistoryAsDeleted() {
        lookupHistoryDao.markAllAsDeleted(true)
    }

    fun undoDeleteAllHistory() {
        lookupHistoryDao.markAllAsDeleted(false)
    }

    fun deleteHistory(mbid: String) {
        lookupHistoryDao.delete(mbid)
    }

    fun deleteAllHistory() {
        lookupHistoryDao.deleteAll()
    }
}
