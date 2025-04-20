package ly.david.musicsearch.shared.domain.browse.usecase

import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.browse.BrowseRemoteCount
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.browse.BrowseEntityCountRepository

class ObserveBrowseEntityCount(
    private val browseEntityCountRepository: BrowseEntityCountRepository,
) {
    operator fun invoke(
        entityId: String,
        entity: MusicBrainzEntity,
    ): Flow<BrowseRemoteCount?> = browseEntityCountRepository.observeBrowseEntityCount(
        entityId,
        entity,
    )
}
