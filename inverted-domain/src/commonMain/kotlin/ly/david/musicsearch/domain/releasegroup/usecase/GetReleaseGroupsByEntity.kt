package ly.david.musicsearch.domain.releasegroup.usecase

import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.domain.releasegroup.ReleaseGroupRepository
import org.koin.core.annotation.Single

@Single
class GetReleaseGroupsByEntity(
    private val releaseGroupRepository: ReleaseGroupRepository,
) {
    operator fun invoke(
        entityId: String,
        entity: MusicBrainzEntity,
        query: String,
        isRemote: Boolean,
        sorted: Boolean,
    ) = releaseGroupRepository.observeReleaseGroupsByEntity(
        entityId = entityId,
        entity = entity,
        query = query,
        isRemote = isRemote,
        sorted = sorted,
    )
}
