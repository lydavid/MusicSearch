package ly.david.musicsearch.shared.domain.releasegroup

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.ListItemModel

interface ReleaseGroupsByEntityRepository {
    fun observeReleaseGroupsByEntity(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): Flow<PagingData<ListItemModel>>

    fun observeCountOfAllReleaseGroups(): Flow<Long>
}
