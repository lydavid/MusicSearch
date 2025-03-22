package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.core.coroutines.CoroutineDispatchers
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToWorkListItemModel
import ly.david.musicsearch.data.musicbrainz.models.core.WorkMusicBrainzModel
import ly.david.musicsearch.shared.domain.listitem.WorkListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.work.WorkDetailsModel
import lydavidmusicsearchdatadatabase.Work
import lydavidmusicsearchdatadatabase.WorkQueries
import lydavidmusicsearchdatadatabase.Works_by_entity

class WorkDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter: WorkQueries = database.workQueries

    fun insert(work: WorkMusicBrainzModel) {
        work.run {
            transacter.insertWork(
                Work(
                    id = id,
                    name = name,
                    disambiguation = disambiguation,
                    type = type,
                    type_id = typeId,
                    language = language,
                    iswcs = iswcs?.sorted()?.toImmutableList(),
                ),
            )
        }
    }

    fun insertAll(works: List<WorkMusicBrainzModel>) {
        transacter.transaction {
            works.forEach { work ->
                insert(work)
            }
        }
    }

    fun getWorkForDetails(workId: String): WorkDetailsModel? {
        return transacter.getWork(
            workId,
            mapper = ::toDetailsModel,
        ).executeAsOneOrNull()
    }

    private fun toDetailsModel(
        id: String,
        name: String,
        disambiguation: String?,
        type: String?,
        language: String?,
        iswcs: List<String>?,
    ) = WorkDetailsModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        language = language,
        iswcs = iswcs,
    )

    fun delete(id: String) {
        transacter.deleteWork(id)
    }

    fun insertWorksByEntity(
        entityId: String,
        workIds: List<String>,
    ): Int {
        return transacter.transactionWithResult {
            workIds.forEach { workId ->
                transacter.insertOrIgnoreWorkByEntity(
                    Works_by_entity(
                        entity_id = entityId,
                        work_id = workId,
                    ),
                )
            }
            workIds.size
        }
    }

    fun deleteWorksByEntity(entityId: String) {
        transacter.deleteWorksByEntity(entityId)
    }

    fun observeCountOfWorksByEntity(entityId: String): Flow<Int> =
        transacter.getNumberOfWorksByEntity(
            entityId = entityId,
            query = "%%",
        )
            .asFlow()
            .mapToOne(coroutineDispatchers.io)
            .map { it.toInt() }

    fun getCountOfWorksByEntity(entityId: String): Int =
        transacter.getNumberOfWorksByEntity(
            entityId = entityId,
            query = "%%",
        )
            .executeAsOne()
            .toInt()

    fun getWorks(
        entityId: String?,
        entity: MusicBrainzEntity?,
        query: String,
    ): PagingSource<Int, WorkListItemModel> = when {
        entityId == null || entity == null -> {
            getAllWorks(query = query)
        }

        entity == MusicBrainzEntity.COLLECTION -> {
            getWorksByCollection(
                collectionId = entityId,
                query = query,
            )
        }

        else -> {
            getWorksByEntity(
                entityId = entityId,
                query = query,
            )
        }
    }

    private fun getAllWorks(
        query: String,
    ): PagingSource<Int, WorkListItemModel> = QueryPagingSource(
        countQuery = transacter.getCountOfAllWorks(
            query = "%$query%",
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
        countQuery = transacter.getNumberOfWorksByEntity(
            entityId = entityId,
            query = "%$query%",
        ),
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
