package ly.david.data.domain.nowplaying

import ly.david.data.core.history.NowPlayingHistory
import ly.david.musicsearch.data.database.dao.NowPlayingHistoryDao
import org.koin.core.annotation.Single

@Single
class NowPlayingHistoryRepository(
    private val nowPlayingHistoryDao: NowPlayingHistoryDao,
) {
    fun upsert(nowPlayingHistory: NowPlayingHistory) {
        nowPlayingHistoryDao.upsert(nowPlayingHistory)
    }

    fun getAllNowPlayingHistory(query: String) =
        nowPlayingHistoryDao.getAllNowPlayingHistory(
            query = "%$query%",
        )

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

    fun delete(raw: String) {
        nowPlayingHistoryDao.delete(raw)
    }

//    fun deleteAllHistory() {
//        nowPlayingHistoryDao.deleteAll()
//    }
}
