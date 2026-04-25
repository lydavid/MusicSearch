package ly.david.musicsearch.shared.domain.work

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.list.ListFilters
import ly.david.musicsearch.shared.domain.listitem.WorkListItemModel

interface WorksListRepository {
    fun observeWorks(
        browseMethod: BrowseMethod,
        listFilters: ListFilters.Works,
    ): Flow<PagingData<WorkListItemModel>>
}
