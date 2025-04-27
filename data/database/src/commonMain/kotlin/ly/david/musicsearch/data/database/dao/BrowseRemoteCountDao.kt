package ly.david.musicsearch.data.database.dao

import app.cash.sqldelight.Query
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ly.david.musicsearch.core.coroutines.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.browse.BrowseRemoteMetadata
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.data.database.Database
import lydavidmusicsearchdatadatabase.Browse_remote_count

// TODO: rename to BrowseRemoteMetadata
class BrowseRemoteCountDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter = database.browse_remote_countQueries

    fun insert(browseRemoteCount: Browse_remote_count) {
        transacter.insert(browseRemoteCount)
    }

    private fun getQuery(
        entityId: String,
        browseEntity: MusicBrainzEntity,
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
        browseEntity: MusicBrainzEntity,
    ): BrowseRemoteMetadata? =
        getQuery(
            entityId = entityId,
            browseEntity = browseEntity,
        ).executeAsOneOrNull()

    fun observe(
        entityId: String,
        browseEntity: MusicBrainzEntity,
    ): Flow<BrowseRemoteMetadata?> =
        getQuery(
            entityId = entityId,
            browseEntity = browseEntity,
        )
            .asFlow()
            .mapToOneOrNull(coroutineDispatchers.io)

//    fun observeLastUpdated(
//        entityId: String,
//        browseEntity: MusicBrainzEntity,
//    ): Flow<Instant?> {
//        val query = transacter.getLastUpdated(
//            entityId = entityId,
//            browseEntity = browseEntity,
//        )
//
//        return query
//            .asFlow()
//            .mapToOneOrNull(coroutineDispatchers.io)
//    }

    fun update(
        entityId: String,
        browseEntity: MusicBrainzEntity,
        remoteCount: Int,
        lastUpdated: Instant = Clock.System.now(),
    ) {
        transacter.transaction {
            transacter.update(
                entityId = entityId,
                browseEntity = browseEntity,
                remoteCount = remoteCount,
                lastUpdated = lastUpdated,
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
