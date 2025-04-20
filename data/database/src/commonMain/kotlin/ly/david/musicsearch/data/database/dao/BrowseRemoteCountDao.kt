package ly.david.musicsearch.data.database.dao

import app.cash.sqldelight.Query
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.core.coroutines.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.browse.BrowseRemoteCount
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.data.database.Database
import lydavidmusicsearchdatadatabase.Browse_entity_count

class BrowseRemoteCountDao(
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
    ): Query<BrowseRemoteCount> =
        transacter.getBrowseEntityCount(
            entityId = entityId,
            browseEntity = browseEntity,
            mapper = { browseEntity, remoteCount ->
                BrowseRemoteCount(
                    browseEntity = browseEntity,
                    remoteCount = remoteCount,
                )
            },
        )

    fun getBrowseEntityCount(
        entityId: String,
        browseEntity: MusicBrainzEntity,
    ): BrowseRemoteCount? =
        getBrowseEntityCountQuery(
            entityId = entityId,
            browseEntity = browseEntity,
        ).executeAsOneOrNull()

    fun getBrowseEntityCountFlow(
        entityId: String,
        browseEntity: MusicBrainzEntity,
    ): Flow<BrowseRemoteCount?> =
        getBrowseEntityCountQuery(
            entityId = entityId,
            browseEntity = browseEntity,
        )
            .asFlow()
            .mapToOneOrNull(coroutineDispatchers.io)

    fun updateBrowseRemoteCount(
        entityId: String,
        browseEntity: MusicBrainzEntity,
        remoteCount: Int,
    ) {
        transacter.transaction {
            transacter.updateBrowseEntityCount(
                entityId = entityId,
                browseEntity = browseEntity,
                remoteCount = remoteCount,
            )
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
