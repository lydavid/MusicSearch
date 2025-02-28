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

    @Suppress("SwallowedException")
    private fun insertLabelByEntity(
        entityId: String,
        labelId: String,
    ): Int {
        return try {
            transacter.insertOrFailLabelByEntity(
                Labels_by_entity(
                    entity_id = entityId,
                    label_id = labelId,
                ),
            )
            1
        } catch (ex: Exception) {
            0
        }
    }

    fun insertLabelsByEntity(
        entityId: String,
        labelIds: List<String>,
    ): Int {
        return transacter.transactionWithResult {
            labelIds.sumOf { labelId ->
                insertLabelByEntity(
                    entityId = entityId,
                    labelId = labelId,
                )
            }
        }
    }

    fun deleteLabelsByEntity(entityId: String) {
        transacter.deleteLabelsByEntity(entityId)
    }

    fun observeCountOfLabelsByEntity(entityId: String): Flow<Int> =
        transacter.getNumberOfLabelsByEntity(
            entityId = entityId,
            query = "%%",
        )
            .asFlow()
            .mapToOne(coroutineDispatchers.io)
            .map { it.toInt() }

    fun getCountOfLabelsByEntity(entityId: String): Int =
        transacter.getNumberOfLabelsByEntity(
            entityId = entityId,
            query = "%%",
        )
            .executeAsOne()
            .toInt()

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
