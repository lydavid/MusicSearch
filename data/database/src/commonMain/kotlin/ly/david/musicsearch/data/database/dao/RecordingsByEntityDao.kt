package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.core.coroutines.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.listitem.RecordingListItemModel
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToRecordingListItemModel
import lydavidmusicsearchdatadatabase.Recordings_by_entity

class RecordingsByEntityDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter = database.recordings_by_entityQueries

    fun insert(
        entityId: String,
        recordingId: String,
    ) {
        transacter.insert(
            Recordings_by_entity(
                entity_id = entityId,
                recording_id = recordingId,
            ),
        )
    }

    fun insertAll(
        entityId: String,
        recordingIds: List<String>,
    ): Int {
        return transacter.transactionWithResult {
            recordingIds.forEach { recordingId ->
                insert(
                    recordingId = recordingId,
                    entityId = entityId,
                )
            }
            recordingIds.size
        }
    }

    fun deleteRecordingsByEntity(entityId: String) {
        transacter.deleteRecordingsByEntity(entityId)
    }

    fun getNumberOfRecordingsByEntity(entityId: String): Flow<Int> =
        transacter.getNumberOfRecordingsByEntity(
            entityId = entityId,
            query = "%%",
        )
            .asFlow()
            .mapToOne(coroutineDispatchers.io)
            .map { it.toInt() }

    fun getRecordingsByEntity(
        entityId: String,
        query: String,
    ): PagingSource<Int, RecordingListItemModel> = QueryPagingSource(
        countQuery = transacter.getNumberOfRecordingsByEntity(
            entityId = entityId,
            query = "%$query%",
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getRecordingsByEntity(
                entityId = entityId,
                query = "%$query%",
                limit = limit,
                offset = offset,
                mapper = ::mapToRecordingListItemModel,
            )
        },
    )
}
