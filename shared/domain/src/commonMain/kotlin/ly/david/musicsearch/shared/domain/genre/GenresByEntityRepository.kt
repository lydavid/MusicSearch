package ly.david.musicsearch.shared.domain.genre

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.GenreListItemModel

interface GenresByEntityRepository {
    fun observeGenresByEntity(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): Flow<PagingData<GenreListItemModel>>
}
