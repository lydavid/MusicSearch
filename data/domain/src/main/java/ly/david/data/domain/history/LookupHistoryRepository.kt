package ly.david.data.domain.history

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.room.history.LookupHistoryDao

@Singleton
class LookupHistoryRepository @Inject constructor(
    private val lookupHistoryDao: LookupHistoryDao,
) {
    fun getAllLookupHistory(query: String, sort: HistorySortOption) =
        lookupHistoryDao.getAllLookupHistory(
            query = "%$query%",
            alphabetically = sort == HistorySortOption.ALPHABETICALLY,
            alphabeticallyReverse = sort == HistorySortOption.ALPHABETICALLY_REVERSE,
            recentlyVisited = sort == HistorySortOption.RECENTLY_VISITED,
            leastRecentlyVisited = sort == HistorySortOption.LEAST_RECENTLY_VISITED,
            mostVisited = sort == HistorySortOption.MOST_VISITED,
            leastVisited = sort == HistorySortOption.LEAST_VISITED,
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
