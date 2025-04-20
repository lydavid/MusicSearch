package ly.david.musicsearch.data.database.dao

import app.cash.sqldelight.Query
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.core.coroutines.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.browse.BrowseRemoteCount
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.data.database.Database
import lydavidmusicsearchdatadatabase.Browse_remote_count

class BrowseRemoteCountDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter = database.browse_remote_countQueries

    fun insert(browseRemoteCount: Browse_remote_count) {
        transacter.insert(browseRemoteCount)
    }

    private fun getBrowseRemoteCountQuery(
        entityId: String,
        browseEntity: MusicBrainzEntity,
    ): Query<BrowseRemoteCount> =
        transacter.getBrowseRemoteCount(
            entityId = entityId,
            browseEntity = browseEntity,
            mapper = { browseEntity, remoteCount ->
                BrowseRemoteCount(
                    browseEntity = browseEntity,
                    remoteCount = remoteCount,
                )
            },
        )

    fun getBrowseRemoteCount(
        entityId: String,
        browseEntity: MusicBrainzEntity,
    ): BrowseRemoteCount? =
        getBrowseRemoteCountQuery(
            entityId = entityId,
            browseEntity = browseEntity,
        ).executeAsOneOrNull()

    fun getBrowseRemoteCountFlow(
        entityId: String,
        browseEntity: MusicBrainzEntity,
    ): Flow<BrowseRemoteCount?> =
        getBrowseRemoteCountQuery(
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
            transacter.updateBrowseRemoteCount(
                entityId = entityId,
                browseEntity = browseEntity,
                remoteCount = remoteCount,
            )
        }
    }

    fun deleteBrowseRemoteCountByEntity(
        entityId: String,
        browseEntity: MusicBrainzEntity,
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
