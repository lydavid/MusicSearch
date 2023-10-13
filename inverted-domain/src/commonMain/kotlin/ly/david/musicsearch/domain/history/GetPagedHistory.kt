package ly.david.musicsearch.domain.history

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.data.core.listitem.ListItemModel
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
