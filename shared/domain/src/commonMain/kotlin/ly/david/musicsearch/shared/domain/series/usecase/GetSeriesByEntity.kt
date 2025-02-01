package ly.david.musicsearch.shared.domain.series.usecase

import androidx.paging.PagingData
import app.cash.paging.cachedIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.base.usecase.GetEntitiesByEntity
import ly.david.musicsearch.shared.domain.listitem.SeriesListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.series.SeriesByEntityRepository

class GetSeriesByEntity(
    private val seriesByEntityRepository: SeriesByEntityRepository,
    private val coroutineScope: CoroutineScope,
) : GetEntitiesByEntity<SeriesListItemModel> {
    override operator fun invoke(
        entityId: String,
        entity: MusicBrainzEntity?,
        listFilters: ListFilters,
    ): Flow<PagingData<SeriesListItemModel>> {
        return when {
            entityId.isEmpty() || entity == null -> emptyFlow()
            else -> seriesByEntityRepository.observeSeriesByEntity(
                entityId = entityId,
                entity = entity,
                listFilters = listFilters,
            )
                .distinctUntilChanged()
                .cachedIn(scope = coroutineScope)
        }
    }
}
