package ly.david.data.domain.browse

import ly.david.data.core.browse.BrowseEntityCount
import ly.david.data.core.network.MusicBrainzEntity
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
