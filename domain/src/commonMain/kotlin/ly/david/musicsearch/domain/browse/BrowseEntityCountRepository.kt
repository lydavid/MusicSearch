package ly.david.musicsearch.domain.browse

import kotlinx.coroutines.flow.Flow
import ly.david.data.core.browse.BrowseEntityCount
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import org.koin.core.annotation.Single

@Single
class BrowseEntityCountRepository(
    private val browseEntityCountDao: BrowseEntityCountDao,
) {
    fun getBrowseEntityCountFlow(entityId: String, entity: MusicBrainzEntity): Flow<BrowseEntityCount?> =
        browseEntityCountDao.getBrowseEntityCountFlow(entityId, entity)

    fun getBrowseEntityCount(entityId: String, entity: MusicBrainzEntity): BrowseEntityCount? =
        browseEntityCountDao.getBrowseEntityCount(entityId, entity)
}
