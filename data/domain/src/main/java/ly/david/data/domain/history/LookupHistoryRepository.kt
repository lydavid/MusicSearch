package ly.david.data.domain.history

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.room.history.LookupHistoryDao

@Singleton
class LookupHistoryRepository @Inject constructor(
    private val lookupHistoryDao: LookupHistoryDao,
) {
    fun getAllLookupHistory(query: String) =
        lookupHistoryDao.getAllLookupHistory("%$query%")

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
