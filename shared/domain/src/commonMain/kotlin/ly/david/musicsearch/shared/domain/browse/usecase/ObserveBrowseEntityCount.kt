package ly.david.musicsearch.shared.domain.browse.usecase

import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.core.models.browse.BrowseEntityCount
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.browse.BrowseEntityCountRepository

class ObserveBrowseEntityCount(
    private val browseEntityCountRepository: BrowseEntityCountRepository,
) {
    operator fun invoke(
        entityId: String,
        entity: MusicBrainzEntity,
    ): Flow<BrowseEntityCount?> = browseEntityCountRepository.observeBrowseEntityCount(
        entityId,
        entity,
    )
}
