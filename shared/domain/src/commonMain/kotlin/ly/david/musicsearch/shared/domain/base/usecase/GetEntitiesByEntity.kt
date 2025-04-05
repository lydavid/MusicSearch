package ly.david.musicsearch.shared.domain.base.usecase

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.ListItemModel

interface GetEntitiesByEntity<LI : ListItemModel> {
    operator fun invoke(
        browseMethod: BrowseMethod?,
        listFilters: ListFilters,
    ): Flow<PagingData<LI>>
}
