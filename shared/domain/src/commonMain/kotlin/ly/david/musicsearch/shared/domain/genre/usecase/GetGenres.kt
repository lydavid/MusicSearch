package ly.david.musicsearch.shared.domain.genre.usecase

import androidx.paging.PagingData
import app.cash.paging.cachedIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.base.usecase.GetEntitiesByEntity
import ly.david.musicsearch.shared.domain.genre.GenresByEntityRepository
import ly.david.musicsearch.shared.domain.listitem.GenreListItemModel

class GetGenres(
    private val genresByEntityRepository: GenresByEntityRepository,
    private val coroutineScope: CoroutineScope,
) : GetEntitiesByEntity<GenreListItemModel> {
    override operator fun invoke(
        browseMethod: BrowseMethod?,
        listFilters: ListFilters,
    ): Flow<PagingData<GenreListItemModel>> {
        return if (browseMethod == null) {
            emptyFlow()
        } else {
            genresByEntityRepository.observeGenresByEntity(
                browseMethod = browseMethod,
                listFilters = listFilters,
            )
                .distinctUntilChanged()
                .cachedIn(coroutineScope)
        }
    }
}
