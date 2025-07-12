package ly.david.musicsearch.shared.domain.work

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.WorkListItemModel

interface WorksListRepository {
    fun observeWorks(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): Flow<PagingData<WorkListItemModel>>

    fun observeCountOfWorks(browseMethod: BrowseMethod): Flow<Int>
}
