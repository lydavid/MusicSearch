package ly.david.musicsearch.shared.domain.area.usecase

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.area.AreasByEntityRepository
import ly.david.musicsearch.shared.domain.base.usecase.GetEntitiesByEntity

class GetAreasByEntity(
    private val areasByEntityRepository: AreasByEntityRepository,
) : GetEntitiesByEntity<AreaListItemModel> {
    override operator fun invoke(
        entityId: String,
        entity: MusicBrainzEntity?,
        listFilters: ListFilters,
    ): Flow<PagingData<AreaListItemModel>> {
        return when {
            entityId.isEmpty() || entity == null -> emptyFlow()
            else -> areasByEntityRepository.observeAreasByEntity(
                entityId = entityId,
                entity = entity,
                listFilters = listFilters,
            )
        }
    }
}
