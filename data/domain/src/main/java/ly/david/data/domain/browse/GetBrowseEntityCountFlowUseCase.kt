package ly.david.data.domain.browse

import kotlinx.coroutines.flow.Flow
import ly.david.data.core.browse.BrowseEntityCount
import ly.david.data.core.network.MusicBrainzEntity
import org.koin.core.annotation.Single

@Single
class GetBrowseEntityCountFlowUseCase(
    private val browseEntityCountRepository: BrowseEntityCountRepository,
) {
    operator fun invoke(
        entityId: String,
        entity: MusicBrainzEntity,
    ): Flow<BrowseEntityCount?> = browseEntityCountRepository.getBrowseEntityCountFlow(entityId, entity)
}
