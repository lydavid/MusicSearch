package ly.david.musicsearch.shared.domain.browse

import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

interface BrowseRemoteMetadataRepository {
    fun observe(
        entityId: String,
        entity: MusicBrainzEntity,
    ): Flow<BrowseRemoteMetadata?>

    fun get(
        entityId: String,
        entity: MusicBrainzEntity,
    ): BrowseRemoteMetadata?
}
