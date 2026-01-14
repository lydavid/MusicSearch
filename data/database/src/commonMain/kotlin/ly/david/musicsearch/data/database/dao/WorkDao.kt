package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.Query
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToWorkListItemModel
import ly.david.musicsearch.data.musicbrainz.models.core.WorkMusicBrainzNetworkModel
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.details.WorkDetailsModel
import ly.david.musicsearch.shared.domain.listitem.WorkListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import lydavidmusicsearchdatadatabase.WorkQueries
import lydavidmusicsearchdatadatabase.Works_by_entity
import kotlin.time.Clock
import kotlin.time.Instant

class WorkDao(
    database: Database,
    private val collectionEntityDao: CollectionEntityDao,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter: WorkQueries = database.workQueries

    fun upsert(
        oldId: String,
        work: WorkMusicBrainzNetworkModel,
    ) {
        work.run {
            if (oldId != id) {
                delete(oldId)
            }
            transacter.upsert(
                id = id,
                name = name,
                disambiguation = disambiguation.orEmpty(),
                type = type.orEmpty(),
                typeId = typeId.orEmpty(),
                languages = languages.orEmpty(),
                iswcs = iswcs?.sorted().orEmpty(),
            )
        }
    }

    fun upsertAll(works: List<WorkMusicBrainzNetworkModel>) {
        transacter.transaction {
            works.forEach { work ->
                upsert(
                    oldId = work.id,
                    work = work,
                )
            }
        }
    }

    fun getWorkForDetails(
        workId: String,
        listenBrainzUsername: String,
    ): WorkDetailsModel? {
        return transacter.getWorkForDetails(
            workId = workId,
            username = listenBrainzUsername,
            mapper = ::toDetailsModel,
        ).executeAsOneOrNull()
    }

    private fun toDetailsModel(
        id: String,
        name: String,
        disambiguation: String,
        type: String,
        languages: List<String>,
        iswcs: List<String>,
        lastUpdated: Instant?,
        listenCount: Long,
    ) = WorkDetailsModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        languages = languages.toPersistentList(),
        iswcs = iswcs.toPersistentList(),
        lastUpdated = lastUpdated ?: Clock.System.now(),
        listenCount = listenCount,
    )

    fun delete(id: String) {
        transacter.deleteWork(id)
        // work attributes are deleted by cascade
    }

    fun insertWorksByEntity(
        entityId: String,
        workIds: List<String>,
    ) {
        transacter.transaction {
            workIds.forEach { workId ->
                transacter.insertOrIgnoreWorkByEntity(
                    Works_by_entity(
                        entity_id = entityId,
                        work_id = workId,
                    ),
                )
            }
        }
    }

    fun deleteWorkLinksByEntity(entityId: String) {
        transacter.deleteWorkLinksByEntity(entityId)
    }

    fun getCountOfWorksByEntity(entityId: String): Int =
        getCountOfWorksByEntityQuery(
            entityId = entityId,
            query = "",
        )
            .executeAsOne()
            .toInt()

    fun getWorks(
        browseMethod: BrowseMethod,
        query: String,
    ): PagingSource<Int, WorkListItemModel> = when (browseMethod) {
        is BrowseMethod.All -> {
            getAllWorks(query = query)
        }

        is BrowseMethod.ByEntity -> {
            if (browseMethod.entityType == MusicBrainzEntityType.COLLECTION) {
                getWorksByCollection(
                    collectionId = browseMethod.entityId,
                    query = query,
                )
            } else {
                getWorksByEntity(
                    entityId = browseMethod.entityId,
                    query = query,
                )
            }
        }
    }

    fun observeCountOfWorks(browseMethod: BrowseMethod): Flow<Int> =
        when (browseMethod) {
            is BrowseMethod.ByEntity -> {
                if (browseMethod.entityType == MusicBrainzEntityType.COLLECTION) {
                    collectionEntityDao.getCountOfEntitiesByCollectionQuery(
                        collectionId = browseMethod.entityId,
                    )
                } else {
                    getCountOfWorksByEntityQuery(
                        entityId = browseMethod.entityId,
                        query = "",
                    )
                }
            }

            else -> {
                getCountOfAllWorks(query = "")
            }
        }
            .asFlow()
            .mapToOne(coroutineDispatchers.io)
            .map { it.toInt() }

    private fun getCountOfAllWorks(
        query: String,
    ): Query<Long> = transacter.getCountOfAllWorks(
        query = "%$query%",
    )

    private fun getAllWorks(
        query: String,
    ): PagingSource<Int, WorkListItemModel> = QueryPagingSource(
        countQuery = getCountOfAllWorks(
            query = query,
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getAllWorks(
                query = "%$query%",
                limit = limit,
                offset = offset,
                mapper = ::mapToWorkListItemModel,
            )
        },
    )

    private fun getWorksByEntity(
        entityId: String,
        query: String,
    ): PagingSource<Int, WorkListItemModel> = QueryPagingSource(
        countQuery = getCountOfWorksByEntityQuery(entityId, query),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getWorksByEntity(
                entityId = entityId,
                query = "%$query%",
                limit = limit,
                offset = offset,
                mapper = ::mapToWorkListItemModel,
            )
        },
    )

    private fun getCountOfWorksByEntityQuery(
        entityId: String,
        query: String,
    ) = transacter.getNumberOfWorksByEntity(
        entityId = entityId,
        query = "%$query%",
    )

    private fun getWorksByCollection(
        collectionId: String,
        query: String,
    ): PagingSource<Int, WorkListItemModel> = QueryPagingSource(
        countQuery = transacter.getNumberOfWorksByCollection(
            collectionId = collectionId,
            query = "%$query%",
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getWorksByCollection(
                collectionId = collectionId,
                query = "%$query%",
                limit = limit,
                offset = offset,
                mapper = ::mapToWorkListItemModel,
            )
        },
    )
}
