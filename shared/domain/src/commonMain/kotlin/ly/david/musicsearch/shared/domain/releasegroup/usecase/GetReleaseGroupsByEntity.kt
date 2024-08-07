package ly.david.musicsearch.shared.domain.releasegroup.usecase

import ly.david.musicsearch.core.models.ListFilters
import ly.david.musicsearch.core.models.listitem.ListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.base.usecase.GetEntitiesByEntity
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupsByEntityRepository

class GetReleaseGroupsByEntity(
    private val releaseGroupsByEntityRepository: ReleaseGroupsByEntityRepository,
) : GetEntitiesByEntity<ListItemModel> {
    override operator fun invoke(
        entityId: String,
        entity: MusicBrainzEntity,
        listFilters: ListFilters,
    ) = releaseGroupsByEntityRepository.observeReleaseGroupsByEntity(
        entityId = entityId,
        entity = entity,
        listFilters = listFilters,
    )
}
