package ly.david.musicsearch.shared.domain.releasegroup

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.ListItemModel

interface ReleaseGroupsListRepository {
    fun observeReleaseGroups(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): Flow<PagingData<ListItemModel>>

    fun observeCountOfReleaseGroups(browseMethod: BrowseMethod?): Flow<Int>

    fun getCountOfEachAlbumType(artistId: String): Flow<List<ReleaseGroupTypeCount>>
}
