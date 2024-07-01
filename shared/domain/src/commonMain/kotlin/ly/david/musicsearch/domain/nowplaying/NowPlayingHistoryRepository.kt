package ly.david.musicsearch.domain.nowplaying

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.core.models.history.NowPlayingHistory
import ly.david.musicsearch.core.models.listitem.ListItemModel

interface NowPlayingHistoryRepository {
    fun upsert(nowPlayingHistory: NowPlayingHistory)
    fun observeNowPlayingHistory(query: String): Flow<PagingData<ListItemModel>>
    fun delete(raw: String)
}
