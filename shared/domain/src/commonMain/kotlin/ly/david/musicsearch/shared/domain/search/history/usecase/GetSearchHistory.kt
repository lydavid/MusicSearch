package ly.david.musicsearch.shared.domain.search.history.usecase

import androidx.paging.PagingData
import app.cash.paging.cachedIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.search.history.SearchHistoryRepository

class GetSearchHistory(
    private val searchHistoryRepository: SearchHistoryRepository,
    private val coroutineScope: CoroutineScope,
) {
    operator fun invoke(
        entity: MusicBrainzEntity,
    ): Flow<PagingData<ListItemModel>> = searchHistoryRepository.observeSearchHistory(entity)
        .distinctUntilChanged()
        .cachedIn(scope = coroutineScope)
}
