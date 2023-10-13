package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.data.core.CoroutineDispatchers
import ly.david.musicsearch.data.core.release.ReleaseForListItem
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToReleaseForListItem
import lydavidmusicsearchdatadatabase.Recording_release

class RecordingReleaseDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter = database.recording_releaseQueries

    fun insert(
        recordingId: String,
        releaseId: String,
    ) {
        transacter.insert(
            Recording_release(
                recording_id = recordingId,
                release_id = releaseId,
            )
        )
    }

    fun insertAll(
        recordingId: String,
        releaseIds: List<String>,
    ) {
        transacter.transaction {
            releaseIds.forEach { releaseId ->
                insert(
                    recordingId = recordingId,
                    releaseId = releaseId,
                )
            }
        }
    }

    fun deleteReleasesByRecording(recordingId: String) {
        withTransaction {
            transacter.deleteReleasesByRecording(recordingId)
            transacter.deleteRecordingReleaseLinks(recordingId)
        }
    }

    fun getNumberOfReleasesByRecording(recordingId: String): Flow<Int> =
        transacter.getNumberOfReleasesByRecording(
            recordingId = recordingId,
            query = "%%",
        )
            .asFlow()
            .mapToOne(coroutineDispatchers.io)
            .map { it.toInt() }

    fun getReleasesByRecording(
        recordingId: String,
        query: String,
    ): PagingSource<Int, ReleaseForListItem> = QueryPagingSource(
        countQuery = transacter.getNumberOfReleasesByRecording(
            recordingId = recordingId,
            query = query,
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
    ) { limit, offset ->
        transacter.getReleasesByRecording(
            recordingId = recordingId,
            query = query,
            limit = limit,
            offset = offset,
            mapper = ::mapToReleaseForListItem,
        )
    }
}
