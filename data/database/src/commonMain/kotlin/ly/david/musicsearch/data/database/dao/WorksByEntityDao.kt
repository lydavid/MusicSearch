package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.core.coroutines.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.listitem.WorkListItemModel
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToWorkListItemModel
import lydavidmusicsearchdatadatabase.Works_by_entity

class WorksByEntityDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter = database.works_by_entityQueries

    fun insert(
        entityId: String,
        workId: String,
    ) {
        transacter.insert(
            Works_by_entity(
                entity_id = entityId,
                work_id = workId,
            ),
        )
    }

    fun insertAll(
        entityId: String,
        workIds: List<String>,
    ): Int {
        return transacter.transactionWithResult {
            workIds.forEach { workId ->
                insert(
                    entityId = entityId,
                    workId = workId,
                )
            }
            workIds.size
        }
    }

    fun deleteWorksByEntity(entityId: String) {
        transacter.deleteWorksByEntity(entityId)
    }

    fun getNumberOfWorksByEntity(entityId: String): Flow<Int> =
        transacter.getNumberOfWorksByEntity(
            entityId = entityId,
            query = "%%",
        )
            .asFlow()
            .mapToOne(coroutineDispatchers.io)
            .map { it.toInt() }

    fun getWorksByEntity(
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
}
