package ly.david.musicsearch.data.repository

import androidx.paging.PagingSource
import ly.david.data.core.history.LookupHistory
import ly.david.data.core.history.LookupHistoryForListItem
import ly.david.musicsearch.data.database.dao.LookupHistoryDao
import ly.david.musicsearch.domain.history.HistorySortOption
import ly.david.musicsearch.domain.history.LookupHistoryRepository

class LookupHistoryRepositoryImpl(
    private val lookupHistoryDao: LookupHistoryDao,
) : LookupHistoryRepository {
    override fun upsert(lookupHistory: LookupHistory) {
        lookupHistoryDao.upsert(lookupHistory)
    }

    override fun getAllLookupHistory(
        query: String,
        sortOption: HistorySortOption
    ): PagingSource<Int, LookupHistoryForListItem> =
        lookupHistoryDao.getAllLookupHistory(
            query = "%$query%",
            alphabetically = sortOption == HistorySortOption.ALPHABETICALLY,
            alphabeticallyReverse = sortOption == HistorySortOption.ALPHABETICALLY_REVERSE,
            recentlyVisited = sortOption == HistorySortOption.RECENTLY_VISITED,
            leastRecentlyVisited = sortOption == HistorySortOption.LEAST_RECENTLY_VISITED,
            mostVisited = sortOption == HistorySortOption.MOST_VISITED,
            leastVisited = sortOption == HistorySortOption.LEAST_VISITED,
        )

    override fun markHistoryAsDeleted(mbid: String) {
        lookupHistoryDao.markAsDeleted(mbid, true)
    }

    override fun undoDeleteHistory(mbid: String) {
        lookupHistoryDao.markAsDeleted(mbid, false)
    }

    override fun markAllHistoryAsDeleted() {
        lookupHistoryDao.markAllAsDeleted(true)
    }

    override fun undoDeleteAllHistory() {
        lookupHistoryDao.markAllAsDeleted(false)
    }

    override fun deleteHistory(mbid: String) {
        lookupHistoryDao.delete(mbid)
    }

    override fun deleteAllHistory() {
        lookupHistoryDao.deleteAll()
    }
}
