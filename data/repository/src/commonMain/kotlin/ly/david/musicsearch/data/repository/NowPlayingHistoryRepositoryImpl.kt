package ly.david.musicsearch.data.repository

import app.cash.paging.Pager
import app.cash.paging.insertSeparators
import app.cash.paging.map
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.shared.domain.common.getDateFormatted
import ly.david.musicsearch.shared.domain.history.NowPlayingHistory
import ly.david.musicsearch.shared.domain.listitem.ListSeparator
import ly.david.musicsearch.shared.domain.listitem.NowPlayingHistoryListItemModel
import ly.david.musicsearch.shared.domain.listitem.toNowPlayingHistoryListItemModel
import ly.david.musicsearch.data.database.dao.NowPlayingHistoryDao
import ly.david.musicsearch.shared.domain.paging.CommonPagingConfig
import ly.david.musicsearch.shared.domain.nowplaying.NowPlayingHistoryRepository

class NowPlayingHistoryRepositoryImpl(
    private val nowPlayingHistoryDao: NowPlayingHistoryDao,
) : NowPlayingHistoryRepository {
    override fun upsert(nowPlayingHistory: NowPlayingHistory) {
        nowPlayingHistoryDao.upsert(nowPlayingHistory)
    }

    override fun observeNowPlayingHistory(query: String) =
        Pager(
            config = CommonPagingConfig.pagingConfig,
            pagingSourceFactory = {
                nowPlayingHistoryDao.getAllNowPlayingHistory(
                    query = query,
                )
            },
        ).flow.map { pagingData ->
            pagingData
                .map(NowPlayingHistory::toNowPlayingHistoryListItemModel)
                .insertSeparators(generator = ::generator)
        }

    private fun generator(
        before: NowPlayingHistoryListItemModel?,
        after: NowPlayingHistoryListItemModel?,
    ): ListSeparator? {
        val beforeDate = before?.lastPlayed?.getDateFormatted()
        val afterDate = after?.lastPlayed?.getDateFormatted()
        return if (beforeDate != afterDate && afterDate != null) {
            ListSeparator(
                id = afterDate,
                text = afterDate,
            )
        } else {
            null
        }
    }

    // TODO: consider whether to support undo delete for now playing history
//    fun markHistoryAsDeleted(mbid: String) {
//        nowPlayingHistoryDao.markAsDeleted(mbid, true)
//    }
//
//    fun undoDeleteHistory(mbid: String) {
//        nowPlayingHistoryDao.markAsDeleted(mbid, false)
//    }
//
//    fun markAllHistoryAsDeleted() {
//        nowPlayingHistoryDao.markAllAsDeleted(true)
//    }
//
//    fun undoDeleteAllHistory() {
//        nowPlayingHistoryDao.markAllAsDeleted(false)
//    }

    override fun delete(raw: String) {
        nowPlayingHistoryDao.delete(raw)
    }

    // TODO: support deleting all now playing history
//    fun deleteAllHistory() {
//        nowPlayingHistoryDao.deleteAll()
//    }
}
