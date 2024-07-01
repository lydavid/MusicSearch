package ly.david.musicsearch.domain.area.usecase

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.core.models.ListFilters
import ly.david.musicsearch.core.models.listitem.AreaListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.domain.area.AreasByEntityRepository
import ly.david.musicsearch.domain.base.usecase.GetEntitiesByEntity
import org.koin.core.annotation.Single

@Single
class GetAreasByEntity(
    private val areasByEntityRepository: AreasByEntityRepository,
) : GetEntitiesByEntity<AreaListItemModel> {
    override operator fun invoke(
        entityId: String,
        entity: MusicBrainzEntity,
        listFilters: ListFilters,
    ): Flow<PagingData<AreaListItemModel>> = areasByEntityRepository.observeAreasByEntity(
        entityId = entityId,
        entity = entity,
        listFilters = listFilters,
    )
}
