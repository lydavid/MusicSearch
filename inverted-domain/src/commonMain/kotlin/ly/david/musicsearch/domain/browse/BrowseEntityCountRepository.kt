package ly.david.musicsearch.domain.browse

import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.data.core.browse.BrowseEntityCount
import ly.david.musicsearch.data.core.network.MusicBrainzEntity

interface BrowseEntityCountRepository {
    fun getBrowseEntityCountFlow(entityId: String, entity: MusicBrainzEntity): Flow<BrowseEntityCount?>
    fun getBrowseEntityCount(entityId: String, entity: MusicBrainzEntity): BrowseEntityCount?
}
