package ly.david.musicsearch.domain.browse.usecase

import ly.david.musicsearch.data.core.browse.BrowseEntityCount
import ly.david.musicsearch.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.domain.browse.BrowseEntityCountRepository
import org.koin.core.annotation.Single

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
