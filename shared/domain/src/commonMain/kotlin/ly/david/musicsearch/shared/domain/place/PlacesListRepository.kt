package ly.david.musicsearch.shared.domain.place

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.list.ListFilters
import ly.david.musicsearch.shared.domain.listitem.PlaceListItemModel

interface PlacesListRepository {
    fun observePlaces(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): Flow<PagingData<PlaceListItemModel>>
}
