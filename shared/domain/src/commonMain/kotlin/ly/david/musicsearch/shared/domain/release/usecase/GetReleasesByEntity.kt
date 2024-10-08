package ly.david.musicsearch.shared.domain.release.usecase

import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.ReleaseListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.base.usecase.GetEntitiesByEntity
import ly.david.musicsearch.shared.domain.release.ReleasesByEntityRepository

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
