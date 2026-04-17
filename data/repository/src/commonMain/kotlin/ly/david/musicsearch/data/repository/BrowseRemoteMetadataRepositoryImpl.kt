package ly.david.musicsearch.data.repository

import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.data.database.dao.BrowseRemoteMetadataDao
import ly.david.musicsearch.shared.domain.browse.BrowseRemoteMetadata
import ly.david.musicsearch.shared.domain.browse.BrowseRemoteMetadataRepository
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType

class BrowseRemoteMetadataRepositoryImpl(
    private val browseRemoteMetadataDao: BrowseRemoteMetadataDao,
) : BrowseRemoteMetadataRepository {
    override fun observe(
        entityId: String,
        browseEntityType: MusicBrainzEntityType,
    ): Flow<BrowseRemoteMetadata?> =
        browseRemoteMetadataDao.observe(
            entityId = entityId,
            browseEntity = browseEntityType,
        )

    override fun get(
        entityId: String,
        browseEntityType: MusicBrainzEntityType,
    ): BrowseRemoteMetadata? =
        browseRemoteMetadataDao.get(
            entityId = entityId,
            browseEntity = browseEntityType,
        )
}
