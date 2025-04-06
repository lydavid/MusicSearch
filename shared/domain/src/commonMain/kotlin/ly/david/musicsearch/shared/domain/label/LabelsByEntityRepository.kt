package ly.david.musicsearch.shared.domain.label

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.LabelListItemModel

interface LabelsByEntityRepository {
    fun observeLabelsByEntity(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): Flow<PagingData<LabelListItemModel>>

    fun observeCountOfAllLabels(): Flow<Long>
}
