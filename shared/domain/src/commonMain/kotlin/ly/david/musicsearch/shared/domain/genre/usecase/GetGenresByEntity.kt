package ly.david.musicsearch.shared.domain.genre.usecase

import androidx.paging.PagingData
import app.cash.paging.cachedIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.base.usecase.GetEntitiesByEntity
import ly.david.musicsearch.shared.domain.genre.GenresByEntityRepository
import ly.david.musicsearch.shared.domain.listitem.GenreListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

class GetGenresByEntity(
    private val genresByEntityRepository: GenresByEntityRepository,
    private val coroutineScope: CoroutineScope,
) : GetEntitiesByEntity<GenreListItemModel> {
    override operator fun invoke(
        entityId: String,
        entity: MusicBrainzEntity?,
        listFilters: ListFilters,
    ): Flow<PagingData<GenreListItemModel>> {
        return when {
            entityId.isEmpty() || entity == null -> emptyFlow()
            else -> genresByEntityRepository.observeGenresByEntity(
                entityId = entityId,
                entity = entity,
                listFilters = listFilters,
            )
                .distinctUntilChanged()
                .cachedIn(coroutineScope)
        }
    }
}
