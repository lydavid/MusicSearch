package ly.david.musicsearch.shared.domain.history.usecase

import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import ly.david.musicsearch.shared.domain.history.HistorySortOption
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.history.LookupHistoryRepository

interface GetPagedHistory {
    operator fun invoke(
        query: String,
        sortOption: HistorySortOption,
    ): Flow<PagingData<ListItemModel>>
}

class GetPagedHistoryImpl(
    private val lookupHistoryRepository: LookupHistoryRepository,
    private val coroutineScope: CoroutineScope,
) : GetPagedHistory {
    override operator fun invoke(
        query: String,
        sortOption: HistorySortOption,
    ): Flow<PagingData<ListItemModel>> {
        return lookupHistoryRepository.observeAllLookupHistory(
            query = query,
            sortOption = sortOption,
        )
            .distinctUntilChanged()
            .cachedIn(scope = coroutineScope)
    }
}
