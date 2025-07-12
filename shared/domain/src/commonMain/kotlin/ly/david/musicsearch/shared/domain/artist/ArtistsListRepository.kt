package ly.david.musicsearch.shared.domain.artist

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.ArtistListItemModel

interface ArtistsListRepository {
    fun observeArtists(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): Flow<PagingData<ArtistListItemModel>>

    fun observeCountOfArtists(browseMethod: BrowseMethod): Flow<Int>
}
