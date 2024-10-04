package ly.david.musicsearch.shared.domain.genre

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.core.models.ListFilters
import ly.david.musicsearch.core.models.listitem.GenreListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity

interface GenresByEntityRepository {
    fun observeGenresByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
        listFilters: ListFilters,
    ): Flow<PagingData<GenreListItemModel>>
}
