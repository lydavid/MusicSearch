package ly.david.musicsearch.data.database.dao

import app.cash.sqldelight.Query
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.data.core.CoroutineDispatchers
import ly.david.musicsearch.data.core.browse.BrowseEntityCount
import ly.david.musicsearch.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.data.database.Database
import lydavidmusicsearchdatadatabase.Browse_entity_count

class BrowseEntityCountDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter = database.browse_entity_countQueries

    fun insert(browseEntityCount: Browse_entity_count) {
        transacter.insert(browseEntityCount)
    }

    private fun getBrowseEntityCountQuery(
        entityId: String,
        browseEntity: MusicBrainzEntity,
    ): Query<BrowseEntityCount> =
        transacter.getBrowseEntityCount(
            entityId = entityId,
            browseEntity = browseEntity,
            mapper = { browseEntity, localCount, remoteCount ->
                BrowseEntityCount(
                    browseEntity = browseEntity,
                    localCount = localCount,
                    remoteCount = remoteCount,
                )
            }
        )

    fun getBrowseEntityCount(
        entityId: String,
        browseEntity: MusicBrainzEntity,
    ): BrowseEntityCount? =
        getBrowseEntityCountQuery(
            entityId = entityId,
            browseEntity = browseEntity,
        ).executeAsOneOrNull()

    fun getBrowseEntityCountFlow(
        entityId: String,
        browseEntity: MusicBrainzEntity,
    ): Flow<BrowseEntityCount?> =
        getBrowseEntityCountQuery(
            entityId = entityId,
            browseEntity = browseEntity,
        )
            .asFlow()
            .mapToOneOrNull(coroutineDispatchers.io)

    private fun updateLocalCountForEntity(
        entityId: String,
        browseEntity: MusicBrainzEntity,
        localCount: Int,
    ) {
        transacter.updateLocalCountForEntity(
            entityId = entityId,
            browseEntity = browseEntity,
            localCount = localCount,
        )
    }

    fun incrementLocalCountForEntity(
        entityId: String,
        browseEntity: MusicBrainzEntity,
        additionalOffset: Int,
    ) {
        transacter.transaction {
            val currentOffset = getBrowseEntityCount(entityId, browseEntity)?.localCount ?: 0
            updateLocalCountForEntity(entityId, browseEntity, currentOffset + additionalOffset)
        }
    }

    fun deleteBrowseEntityCountByEntity(
        entityId: String,
        browseEntity: MusicBrainzEntity,
    ) {
        transacter.deleteBrowseEntityCountByEntity(
            entityId = entityId,
            browseEntity = browseEntity,
        )
    }

    fun deleteAllBrowseEntityCountByRemoteCollections() {
        transacter.deleteAllBrowseEntityCountByRemoteCollections()
    }
}
