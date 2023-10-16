package ly.david.musicsearch.domain.releasegroup.usecase

import ly.david.musicsearch.core.models.ListFilters
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.domain.releasegroup.ReleaseGroupsByEntityRepository
import org.koin.core.annotation.Single

@Single
class GetReleaseGroupsByEntity(
    private val releaseGroupsByEntityRepository: ReleaseGroupsByEntityRepository,
) {
    operator fun invoke(
        entityId: String,
        entity: MusicBrainzEntity,
        listFilters: ListFilters,
    ) = releaseGroupsByEntityRepository.observeReleaseGroupsByEntity(
        entityId = entityId,
        entity = entity,
        listFilters = listFilters,
    )
}
