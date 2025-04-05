package ly.david.musicsearch.shared.domain.series

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.SeriesListItemModel

interface SeriesByEntityRepository {
    fun observeSeriesByEntity(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): Flow<PagingData<SeriesListItemModel>>
}
