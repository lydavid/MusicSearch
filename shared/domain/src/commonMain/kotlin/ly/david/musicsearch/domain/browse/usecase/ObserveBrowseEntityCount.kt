package ly.david.musicsearch.domain.browse.usecase

import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.core.models.browse.BrowseEntityCount
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.domain.browse.BrowseEntityCountRepository
import org.koin.core.annotation.Single

@Single
class ObserveBrowseEntityCount(
    private val browseEntityCountRepository: BrowseEntityCountRepository,
) {
    operator fun invoke(
        entityId: String,
        entity: MusicBrainzEntity,
    ): Flow<BrowseEntityCount?> = browseEntityCountRepository.observeBrowseEntityCount(entityId, entity)
}
