package ly.david.musicsearch.domain.release.usecase

import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.domain.release.ReleaseRepository
import org.koin.core.annotation.Single

@Single
class GetReleasesByEntity(
    private val releaseRepository: ReleaseRepository,
) {
    operator fun invoke(
        entityId: String,
        entity: MusicBrainzEntity,
        query: String,
        isRemote: Boolean,
    ) = releaseRepository.observeReleasesByEntity(
        entityId = entityId,
        entity = entity,
        query = query,
        isRemote = isRemote,
    )
}
