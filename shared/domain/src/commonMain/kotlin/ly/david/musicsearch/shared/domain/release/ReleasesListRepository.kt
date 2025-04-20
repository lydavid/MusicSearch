package ly.david.musicsearch.shared.domain.release

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.ReleaseListItemModel

interface ReleasesListRepository {
    fun observeReleases(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): Flow<PagingData<ReleaseListItemModel>>

    fun observeCountOfAllReleases(): Flow<Long>
}
