package ly.david.musicsearch.shared.domain.nowplaying

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.history.NowPlayingHistory
import ly.david.musicsearch.shared.domain.listitem.ListItemModel

interface NowPlayingHistoryRepository {
    fun upsert(nowPlayingHistory: NowPlayingHistory)
    fun observeNowPlayingHistory(query: String): Flow<PagingData<ListItemModel>>
    fun delete(raw: String)
}
