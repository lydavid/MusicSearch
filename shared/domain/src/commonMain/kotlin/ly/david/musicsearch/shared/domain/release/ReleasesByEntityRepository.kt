package ly.david.musicsearch.shared.domain.release

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.ReleaseListItemModel

// TODO: rename
interface ReleasesByEntityRepository {
    fun observeReleasesByEntity(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): Flow<PagingData<ReleaseListItemModel>>

    fun observeCountOfAllReleases(): Flow<Long>
}
