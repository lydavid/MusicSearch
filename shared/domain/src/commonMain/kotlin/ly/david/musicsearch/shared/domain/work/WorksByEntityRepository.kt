package ly.david.musicsearch.shared.domain.work

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.WorkListItemModel

interface WorksByEntityRepository {
    fun observeWorksByEntity(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): Flow<PagingData<WorkListItemModel>>

    fun observeCountOfAllWorks(): Flow<Long>
}
