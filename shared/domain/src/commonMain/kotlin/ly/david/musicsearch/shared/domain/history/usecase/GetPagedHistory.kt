package ly.david.musicsearch.shared.domain.history.usecase

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.core.models.listitem.ListItemModel
import ly.david.musicsearch.core.models.history.HistorySortOption
import ly.david.musicsearch.shared.domain.history.LookupHistoryRepository
import org.koin.core.annotation.Single

@Single
class GetPagedHistory(
    private val lookupHistoryRepository: LookupHistoryRepository,
) {
    operator fun invoke(
        query: String,
        sortOption: HistorySortOption,
    ): Flow<PagingData<ListItemModel>> =
        lookupHistoryRepository.observeAllLookupHistory(query, sortOption)
}
