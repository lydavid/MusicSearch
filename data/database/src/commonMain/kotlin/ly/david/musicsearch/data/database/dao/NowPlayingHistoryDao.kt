package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.paging3.QueryPagingSource
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.history.NowPlayingHistory
import ly.david.musicsearch.data.database.Database

class NowPlayingHistoryDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter = database.now_playing_historyQueries

    fun upsert(nowPlayingHistory: NowPlayingHistory) {
        nowPlayingHistory.run {
            transacter.upsert(
                raw = raw,
                lastPlayed = lastPlayed,
            )
        }
    }

    fun getAllNowPlayingHistory(
        query: String,
    ): PagingSource<Int, NowPlayingHistory> = QueryPagingSource(
        countQuery = transacter.getAllNowPlayingHistoryCount(
            query = "%$query%",
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getAllNowPlayingHistory(
                query = "%$query%",
                limit = limit,
                offset = offset,
                mapper = { raw, lastPlayed ->
                    NowPlayingHistory(
                        raw = raw,
                        lastPlayed = lastPlayed,
                    )
                },
            )
        },
    )

//    fun markAsDeleted(mbid: String, deleted: Boolean) {
//        transacter.markAsDeleted(
//            mbid = mbid,
//            deleted = deleted,
//        )
//    }
//
//    fun markAllAsDeleted(deleted: Boolean) {
//        transacter.markAllAsDeleted(deleted)
//    }

    fun delete(raw: String) {
        transacter.delete(raw)
    }

//    fun deleteAll() {
//        transacter.deleteAll()
//    }
}
