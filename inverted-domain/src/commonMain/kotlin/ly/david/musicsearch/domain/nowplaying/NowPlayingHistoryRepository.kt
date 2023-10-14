package ly.david.musicsearch.domain.nowplaying

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.data.core.history.NowPlayingHistory
import ly.david.musicsearch.data.core.listitem.ListItemModel

interface NowPlayingHistoryRepository {
    fun upsert(nowPlayingHistory: NowPlayingHistory)
    fun observeNowPlayingHistory(query: String): Flow<PagingData<ListItemModel>>
    fun delete(raw: String)
}
