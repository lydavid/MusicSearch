package ly.david.musicsearch.shared.domain.browse

import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

interface BrowseEntityCountRepository {
    fun observeBrowseEntityCount(entityId: String, entity: MusicBrainzEntity): Flow<BrowseRemoteCount?>
    fun getBrowseEntityCount(entityId: String, entity: MusicBrainzEntity): BrowseRemoteCount?
}
