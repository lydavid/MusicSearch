package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.core.coroutines.CoroutineDispatchers
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToReleaseListItemModel
import ly.david.musicsearch.shared.domain.listitem.ReleaseListItemModel
import lydavidmusicsearchdatadatabase.Recording_release

class RecordingReleaseDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter = database.recording_releaseQueries

    @Suppress("SwallowedException")
    private fun insert(
        recordingId: String,
        releaseId: String,
    ): Int {
        return try {
            transacter.insertOrFail(
                Recording_release(
                    recording_id = recordingId,
                    release_id = releaseId,
                ),
            )
            1
        } catch (ex: Exception) {
            0
        }
    }

    fun insertAll(
        recordingId: String,
        releaseIds: List<String>,
    ): Int {
        return transacter.transactionWithResult {
            releaseIds.sumOf { releaseId ->
                insert(
                    recordingId = recordingId,
                    releaseId = releaseId,
                )
            }
        }
    }

    fun deleteReleasesByRecording(recordingId: String) {
        transacter.deleteReleasesByRecording(recordingId)
    }

    fun observeCountOfReleasesByRecording(recordingId: String): Flow<Int> =
        transacter.getNumberOfReleasesByRecording(
            recordingId = recordingId,
            query = "%%",
        )
            .asFlow()
            .mapToOne(coroutineDispatchers.io)
            .map { it.toInt() }

    fun getCountOfReleasesByRecording(recordingId: String): Int =
        transacter.getNumberOfReleasesByRecording(
            recordingId = recordingId,
            query = "%%",
        )
            .executeAsOne()
            .toInt()

    fun getReleasesByRecording(
        recordingId: String,
        query: String,
    ): PagingSource<Int, ReleaseListItemModel> = QueryPagingSource(
        countQuery = transacter.getNumberOfReleasesByRecording(
            recordingId = recordingId,
            query = "%$query%",
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getReleasesByRecording(
                recordingId = recordingId,
                query = "%$query%",
                limit = limit,
                offset = offset,
                mapper = ::mapToReleaseListItemModel,
            )
        },
    )
}
