package ly.david.musicsearch.shared.domain.artist

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.ArtistListItemModel

interface ArtistsByEntityRepository {
    fun observeArtistsByEntity(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): Flow<PagingData<ArtistListItemModel>>
}
