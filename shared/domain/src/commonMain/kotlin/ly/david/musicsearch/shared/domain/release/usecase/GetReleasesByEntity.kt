package ly.david.musicsearch.shared.domain.release.usecase

import ly.david.musicsearch.core.models.ListFilters
import ly.david.musicsearch.core.models.listitem.ReleaseListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.base.usecase.GetEntitiesByEntity
import ly.david.musicsearch.shared.domain.release.ReleasesByEntityRepository
import org.koin.core.annotation.Single

@Single
class GetReleasesByEntity(
    private val releasesByEntityRepository: ReleasesByEntityRepository,
) : GetEntitiesByEntity<ReleaseListItemModel> {
    override operator fun invoke(
        entityId: String,
        entity: MusicBrainzEntity,
        listFilters: ListFilters,
    ) = releasesByEntityRepository.observeReleasesByEntity(
        entityId = entityId,
        entity = entity,
        listFilters = listFilters,
    )
}
