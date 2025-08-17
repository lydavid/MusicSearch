package ly.david.musicsearch.data.database.dao

import app.cash.sqldelight.Query
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import kotlinx.coroutines.flow.Flow
import kotlin.time.Clock
import kotlin.time.Instant
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.browse.BrowseRemoteMetadata
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.data.database.Database

class BrowseRemoteMetadataDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter = database.browse_remote_metadataQueries

    fun upsert(
        entityId: String,
        browseEntity: MusicBrainzEntityType,
        remoteCount: Int,
        lastUpdated: Instant = Clock.System.now(),
    ) {
        transacter.upsert(
            entityId = entityId,
            browseEntity = browseEntity,
            remoteCount = remoteCount,
            lastUpdated = lastUpdated,
        )
    }

    private fun getQuery(
        entityId: String,
        browseEntity: MusicBrainzEntityType,
    ): Query<BrowseRemoteMetadata> =
        transacter.getBrowseRemoteMetadata(
            entityId = entityId,
            browseEntity = browseEntity,
            mapper = { remoteCount, lastUpdated ->
                BrowseRemoteMetadata(
                    remoteCount = remoteCount,
                    lastUpdated = lastUpdated,
                )
            },
        )

    fun get(
        entityId: String,
        browseEntity: MusicBrainzEntityType,
    ): BrowseRemoteMetadata? =
        getQuery(
            entityId = entityId,
            browseEntity = browseEntity,
        ).executeAsOneOrNull()

    fun observe(
        entityId: String,
        browseEntity: MusicBrainzEntityType,
    ): Flow<BrowseRemoteMetadata?> =
        getQuery(
            entityId = entityId,
            browseEntity = browseEntity,
        )
            .asFlow()
            .mapToOneOrNull(coroutineDispatchers.io)

    fun deleteBrowseRemoteCountByEntity(
        entityId: String,
        browseEntity: MusicBrainzEntityType,
    ) {
        transacter.deleteBrowseRemoteCountByEntity(
            entityId = entityId,
            browseEntity = browseEntity,
        )
    }

    fun deleteAllBrowseRemoteCountByRemoteCollections() {
        transacter.deleteAllBrowseRemoteCountByRemoteCollections()
    }
}
