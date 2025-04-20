package ly.david.musicsearch.data.repository

import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.browse.BrowseRemoteCount
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.data.database.dao.BrowseRemoteCountDao
import ly.david.musicsearch.shared.domain.browse.BrowseEntityCountRepository

class BrowseEntityCountRepositoryImpl(
    private val browseEntityCountDao: BrowseRemoteCountDao,
) : BrowseEntityCountRepository {
    override fun observeBrowseEntityCount(entityId: String, entity: MusicBrainzEntity): Flow<BrowseRemoteCount?> =
        browseEntityCountDao.getBrowseRemoteCountFlow(entityId, entity)

    override fun getBrowseEntityCount(entityId: String, entity: MusicBrainzEntity): BrowseRemoteCount? =
        browseEntityCountDao.getBrowseRemoteCount(entityId, entity)
}
