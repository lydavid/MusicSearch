package ly.david.musicsearch.domain.browse

import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.core.models.browse.BrowseEntityCount
import ly.david.musicsearch.core.models.network.MusicBrainzEntity

interface BrowseEntityCountRepository {
    fun observeBrowseEntityCount(entityId: String, entity: MusicBrainzEntity): Flow<BrowseEntityCount?>
    fun getBrowseEntityCount(entityId: String, entity: MusicBrainzEntity): BrowseEntityCount?
}
