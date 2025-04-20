package ly.david.musicsearch.shared.domain.area

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel

interface AreasListRepository {
    fun observeAreas(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): Flow<PagingData<AreaListItemModel>>

    fun observeCountOfAllAreas(): Flow<Long>
}
