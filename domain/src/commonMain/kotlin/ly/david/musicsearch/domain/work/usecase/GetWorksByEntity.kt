package ly.david.musicsearch.domain.work.usecase

import ly.david.musicsearch.core.models.ListFilters
import ly.david.musicsearch.core.models.listitem.WorkListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.domain.base.GetEntitiesByEntity
import ly.david.musicsearch.domain.work.WorksByEntityRepository
import org.koin.core.annotation.Single

@Single
class GetWorksByEntity(
    private val worksByEntityRepository: WorksByEntityRepository,
) : GetEntitiesByEntity<WorkListItemModel> {
    override operator fun invoke(
        entityId: String,
        entity: MusicBrainzEntity,
        listFilters: ListFilters,
    ) = worksByEntityRepository.observeWorksByEntity(
        entityId = entityId,
        entity = entity,
        listFilters = listFilters,
    )
}
