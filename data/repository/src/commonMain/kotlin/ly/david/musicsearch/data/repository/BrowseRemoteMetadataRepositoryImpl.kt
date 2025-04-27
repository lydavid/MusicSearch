package ly.david.musicsearch.data.repository

import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.data.database.dao.BrowseRemoteCountDao
import ly.david.musicsearch.shared.domain.browse.BrowseRemoteMetadata
import ly.david.musicsearch.shared.domain.browse.BrowseRemoteMetadataRepository
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

class BrowseRemoteMetadataRepositoryImpl(
    private val browseEntityCountDao: BrowseRemoteCountDao,
) : BrowseRemoteMetadataRepository {
    override fun observe(
        entityId: String,
        entity: MusicBrainzEntity,
    ): Flow<BrowseRemoteMetadata?> =
        browseEntityCountDao.observe(entityId, entity)

    override fun get(
        entityId: String,
        entity: MusicBrainzEntity,
    ): BrowseRemoteMetadata? =
        browseEntityCountDao.get(entityId, entity)
}
