package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.core.models.CoroutineDispatchers
import ly.david.musicsearch.core.models.listitem.RecordingListItemModel
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToRecordingListItemModel
import lydavidmusicsearchdatadatabase.Recording_work
import lydavidmusicsearchdatadatabase.Recording_workQueries

class RecordingWorkDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter: Recording_workQueries = database.recording_workQueries

    fun insert(
        recordingId: String,
        workId: String,
    ) {
        transacter.insert(
            Recording_work(
                recording_id = recordingId,
                work_id = workId,
            ),
        )
    }

    fun insertAll(
        workId: String,
        recordingIds: List<String>,
    ) {
        transacter.transaction {
            recordingIds.forEach { recordingId ->
                insert(
                    recordingId = recordingId,
                    workId = workId,
                )
            }
        }
    }

    fun deleteRecordingsByWork(workId: String) {
        transacter.deleteRecordingsByWork(workId)
    }

    fun getNumberOfRecordingsByWork(workId: String): Flow<Int> =
        transacter.getNumberOfRecordingsByWork(
            workId = workId,
            query = "%%",
        )
            .asFlow()
            .mapToOne(coroutineDispatchers.io)
            .map { it.toInt() }

    fun getRecordingsByWork(
        workId: String,
        query: String,
    ): PagingSource<Int, RecordingListItemModel> = QueryPagingSource(
        countQuery = transacter.getNumberOfRecordingsByWork(
            workId = workId,
            query = query,
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
    ) { limit, offset ->
        transacter.getRecordingsByWork(
            workId = workId,
            query = query,
            limit = limit,
            offset = offset,
            mapper = ::mapToRecordingListItemModel,
        )
    }
}
