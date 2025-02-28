package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.core.coroutines.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.listitem.LabelListItemModel
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToLabelListItemModel
import lydavidmusicsearchdatadatabase.Labels_by_entity

class LabelsByEntityDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter = database.labels_by_entityQueries

    fun insert(
        entityId: String,
        labelId: String,
    ) {
        transacter.insert(
            Labels_by_entity(
                entity_id = entityId,
                label_id = labelId,
            ),
        )
    }

    fun insertAll(
        entityId: String,
        labelIds: List<String>,
    ): Int {
        return transacter.transactionWithResult {
            labelIds.forEach { labelId ->
                insert(
                    entityId = entityId,
                    labelId = labelId,
                )
            }
            labelIds.size
        }
    }

    fun deleteLabelsByEntity(entityId: String) {
        transacter.deleteLabelsByEntity(entityId)
    }

    fun getNumberOfLabelsByEntity(entityId: String): Flow<Int> =
        transacter.getNumberOfLabelsByEntity(
            entityId = entityId,
            query = "%%",
        )
            .asFlow()
            .mapToOne(coroutineDispatchers.io)
            .map { it.toInt() }

    fun getLabelsByEntity(
        entityId: String,
        query: String,
    ): PagingSource<Int, LabelListItemModel> = QueryPagingSource(
        countQuery = transacter.getNumberOfLabelsByEntity(
            entityId = entityId,
            query = "%$query%",
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getLabelsByEntity(
                entityId = entityId,
                query = "%$query%",
                limit = limit,
                offset = offset,
                mapper = ::mapToLabelListItemModel,
            )
        },
    )
}
