package ly.david.musicsearch.shared.domain.nowplaying.usecase

import androidx.paging.PagingData
import app.cash.paging.cachedIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.nowplaying.NowPlayingHistoryRepository

class GetNowPlayingHistory(
    private val nowPlayingHistoryRepository: NowPlayingHistoryRepository,
    private val coroutineScope: CoroutineScope,
) {
    operator fun invoke(
        query: String,
    ): Flow<PagingData<ListItemModel>> =
        nowPlayingHistoryRepository.observeNowPlayingHistory(query)
            .distinctUntilChanged()
            .cachedIn(scope = coroutineScope)
}
