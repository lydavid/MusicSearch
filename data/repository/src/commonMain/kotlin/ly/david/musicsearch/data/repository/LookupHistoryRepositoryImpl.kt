package ly.david.musicsearch.data.repository

import app.cash.paging.Pager
import app.cash.paging.PagingData
import app.cash.paging.insertSeparators
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.data.database.dao.LookupHistoryDao
import ly.david.musicsearch.shared.domain.common.getDateFormatted
import ly.david.musicsearch.shared.domain.history.HistorySortOption
import ly.david.musicsearch.shared.domain.history.LookupHistory
import ly.david.musicsearch.shared.domain.history.LookupHistoryRepository
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.listitem.ListSeparator
import ly.david.musicsearch.shared.domain.listitem.LookupHistoryListItemModel
import ly.david.musicsearch.shared.domain.paging.CommonPagingConfig

class LookupHistoryRepositoryImpl(
    private val lookupHistoryDao: LookupHistoryDao,
) : LookupHistoryRepository {
    override fun upsert(
        oldId: String,
        lookupHistory: LookupHistory,
    ) {
        lookupHistoryDao.upsert(
            oldId = oldId,
            lookupHistory = lookupHistory,
        )
    }

    override fun observeAllLookupHistory(
        query: String,
        sortOption: HistorySortOption,
    ): Flow<PagingData<ListItemModel>> =
        Pager(
            config = CommonPagingConfig.pagingConfig,
            pagingSourceFactory = {
                lookupHistoryDao.getAllLookupHistory(
                    query = "%$query%",
                    alphabetically = sortOption == HistorySortOption.ALPHABETICALLY,
                    alphabeticallyReverse = sortOption == HistorySortOption.ALPHABETICALLY_REVERSE,
                    recentlyVisited = sortOption == HistorySortOption.RECENTLY_VISITED,
                    leastRecentlyVisited = sortOption == HistorySortOption.LEAST_RECENTLY_VISITED,
                    mostVisited = sortOption == HistorySortOption.MOST_VISITED,
                    leastVisited = sortOption == HistorySortOption.LEAST_VISITED,
                )
            },
        ).flow.map { pagingData ->
            pagingData
                .insertSeparators {
                        before: LookupHistoryListItemModel?,
                        after: LookupHistoryListItemModel?,
                    ->
                    getListSeparator(
                        before = before,
                        after = after,
                        sortOption = sortOption,
                    )
                }
        }

    private fun getListSeparator(
        before: LookupHistoryListItemModel?,
        after: LookupHistoryListItemModel?,
        sortOption: HistorySortOption,
    ): ListSeparator? {
        if (sortOption != HistorySortOption.RECENTLY_VISITED &&
            sortOption != HistorySortOption.LEAST_RECENTLY_VISITED
        ) {
            return null
        }

        val beforeDate = before?.lastAccessed?.getDateFormatted()
        val afterDate = after?.lastAccessed?.getDateFormatted()
        if (beforeDate == afterDate || afterDate == null) {
            return null
        }

        return ListSeparator(
            id = after.lastAccessed.epochSeconds.toString(),
            text = afterDate,
        )
    }

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
