package ly.david.musicsearch.shared.domain.place

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.PlaceListItemModel

interface PlacesByEntityRepository {
    fun observePlacesByEntity(
        browseMethod: BrowseMethod,
        listFilters: ListFilters = ListFilters(),
    ): Flow<PagingData<PlaceListItemModel>>
}
