package ly.david.musicsearch.domain.history.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.data.core.listitem.ListItemModel
import ly.david.musicsearch.data.core.history.HistorySortOption
import ly.david.musicsearch.domain.history.LookupHistoryRepository
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
