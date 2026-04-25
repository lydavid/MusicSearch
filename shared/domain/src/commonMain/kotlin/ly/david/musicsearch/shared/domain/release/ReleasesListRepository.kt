package ly.david.musicsearch.shared.domain.release

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.list.ListFilters
import ly.david.musicsearch.shared.domain.listitem.ReleaseListItemModel
import kotlin.time.Instant

interface ReleasesListRepository {
    fun observeReleases(
        browseMethod: BrowseMethod,
        listFilters: ListFilters.Releases,
        now: Instant,
    ): Flow<PagingData<ReleaseListItemModel>>
}
