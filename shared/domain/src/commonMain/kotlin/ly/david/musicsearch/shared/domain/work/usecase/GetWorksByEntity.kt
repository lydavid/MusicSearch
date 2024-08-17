package ly.david.musicsearch.shared.domain.work.usecase

import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.WorkListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.base.usecase.GetEntitiesByEntity
import ly.david.musicsearch.shared.domain.work.WorksByEntityRepository

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
