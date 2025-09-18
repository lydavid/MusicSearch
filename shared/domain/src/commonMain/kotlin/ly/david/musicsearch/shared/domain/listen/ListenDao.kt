package ly.david.musicsearch.shared.domain.listen

import app.cash.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.recording.RecordingFacet

interface ListenDao {
    fun insert(
        listens: List<Listen>,
    )

    fun deleteListensByUser(username: String)

    fun observeUnfilteredCountOfListensByUser(username: String): Flow<Long?>

    fun getListensByUser(
        username: String,
        query: String,
        recordingId: String?,
    ): PagingSource<Int, ListenListItemModel>

    fun getRecordingFacetsByUser(
        username: String,
        query: String,
    ): PagingSource<Int, RecordingFacet>

    fun getLatestTimestampMsByUser(username: String): Long?

    fun getOldestTimestampMsByUser(username: String): Long?
}
