package ly.david.musicsearch.domain.browse.usecase

import ly.david.musicsearch.core.models.browse.BrowseEntityCount
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.domain.browse.BrowseEntityCountRepository
import org.koin.core.annotation.Single

// TODO: if we exclusively use this in repository, then remove
/**
 * Prefer to use [GetBrowseEntityCountFlowUseCase].
 */
@Single
class GetBrowseEntityCountUseCase(
    private val browseEntityCountRepository: BrowseEntityCountRepository,
) {
    operator fun invoke(
        entityId: String,
        entity: MusicBrainzEntity,
    ): BrowseEntityCount? = browseEntityCountRepository.getBrowseEntityCount(entityId, entity)
}
