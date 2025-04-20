package ly.david.musicsearch.shared.domain.genre

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.GenreListItemModel

interface GenresListRepository {
    fun observeGenres(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): Flow<PagingData<GenreListItemModel>>

    fun observeCountOfAllGenres(): Flow<Long>
}
