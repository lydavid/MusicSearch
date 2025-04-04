package ly.david.musicsearch.shared.domain.area.usecase

import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.area.AreasByEntityRepository
import ly.david.musicsearch.shared.domain.base.usecase.GetEntitiesByEntity

interface GetAreas {
    operator fun invoke(
        entityId: String,
        entity: MusicBrainzEntity?,
        listFilters: ListFilters,
    ): Flow<PagingData<AreaListItemModel>>
}

class GetAreasImpl(
    private val areasByEntityRepository: AreasByEntityRepository,
    private val coroutineScope: CoroutineScope,
) : GetEntitiesByEntity<AreaListItemModel>, GetAreas {
    override operator fun invoke(
        entityId: String,
        entity: MusicBrainzEntity?,
        listFilters: ListFilters,
    ): Flow<PagingData<AreaListItemModel>> {
        return areasByEntityRepository.observeAreasByEntity(
            entityId = entityId,
            entity = entity,
            listFilters = listFilters,
        )
            .distinctUntilChanged()
            .cachedIn(scope = coroutineScope)
    }
}
