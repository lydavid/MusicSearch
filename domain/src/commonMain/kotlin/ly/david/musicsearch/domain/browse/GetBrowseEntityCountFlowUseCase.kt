package ly.david.musicsearch.domain.browse

import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.data.core.browse.BrowseEntityCount
import ly.david.musicsearch.data.core.network.MusicBrainzEntity
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
