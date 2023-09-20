package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.coroutines.Dispatchers
import ly.david.data.core.RecordingForListItem
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToRecordingForListItem
import lydavidmusicsearchdatadatabase.Recording_work
import lydavidmusicsearchdatadatabase.Recording_workQueries

class RecordingWorkDao(
    database: Database,
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
            )
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

    fun getNumberOfRecordingsByWork(workId: String): Int =
        transacter.getNumberOfRecordingsByWork(
            workId = workId,
            query = "%%",
        ).executeAsOne().toInt()

    fun getRecordingsByWork(
        workId: String,
        query: String,
    ): PagingSource<Int, RecordingForListItem> = QueryPagingSource(
        countQuery = transacter.getNumberOfRecordingsByWork(
            workId = workId,
            query = query,
        ),
        transacter = transacter,
        context = Dispatchers.IO,
    ) { limit, offset ->
        transacter.getRecordingsByWork(
            workId = workId,
            query = query,
            limit = limit,
            offset = offset,
            mapper = ::mapToRecordingForListItem,
        )
    }
}
