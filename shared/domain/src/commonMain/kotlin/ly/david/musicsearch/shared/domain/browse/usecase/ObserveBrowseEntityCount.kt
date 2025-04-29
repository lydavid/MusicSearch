package ly.david.musicsearch.shared.domain.browse.usecase

import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.browse.BrowseRemoteMetadata
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.browse.BrowseRemoteMetadataRepository

class ObserveBrowseEntityCount(
    private val browseEntityCountRepository: BrowseRemoteMetadataRepository,
) {
    operator fun invoke(
        entityId: String,
        entity: MusicBrainzEntity,
    ): Flow<BrowseRemoteMetadata?> = browseEntityCountRepository.observe(
        entityId,
        entity,
    )
}
