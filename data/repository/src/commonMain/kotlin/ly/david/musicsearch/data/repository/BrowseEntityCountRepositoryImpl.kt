package ly.david.musicsearch.data.repository

import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.data.core.browse.BrowseEntityCount
import ly.david.musicsearch.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.domain.browse.BrowseEntityCountRepository

class BrowseEntityCountRepositoryImpl(
    private val browseEntityCountDao: BrowseEntityCountDao,
) : BrowseEntityCountRepository {
    override fun getBrowseEntityCountFlow(entityId: String, entity: MusicBrainzEntity): Flow<BrowseEntityCount?> =
        browseEntityCountDao.getBrowseEntityCountFlow(entityId, entity)

    override fun getBrowseEntityCount(entityId: String, entity: MusicBrainzEntity): BrowseEntityCount? =
        browseEntityCountDao.getBrowseEntityCount(entityId, entity)
}
