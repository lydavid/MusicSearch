package ly.david.musicsearch.shared.domain.browse

import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType

interface BrowseRemoteMetadataRepository {
    fun observe(
        entityId: String,
        entity: MusicBrainzEntityType,
    ): Flow<BrowseRemoteMetadata?>

    fun get(
        entityId: String,
        entity: MusicBrainzEntityType,
    ): BrowseRemoteMetadata?
}
