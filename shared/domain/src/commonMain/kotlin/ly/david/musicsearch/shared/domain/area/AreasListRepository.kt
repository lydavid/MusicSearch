package ly.david.musicsearch.shared.domain.area

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.list.ListFilters
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel

interface AreasListRepository {
    fun observeAreas(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): Flow<PagingData<AreaListItemModel>>
}
