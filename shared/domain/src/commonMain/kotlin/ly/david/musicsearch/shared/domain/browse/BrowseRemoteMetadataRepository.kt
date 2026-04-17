package ly.david.musicsearch.shared.domain.browse

import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType

interface BrowseRemoteMetadataRepository {
    fun observe(
        entityId: String,
        browseEntityType: MusicBrainzEntityType,
    ): Flow<BrowseRemoteMetadata?>

    fun get(
        entityId: String,
        browseEntityType: MusicBrainzEntityType,
    ): BrowseRemoteMetadata?
}
