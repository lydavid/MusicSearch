package ly.david.musicsearch.shared.domain.listen

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.Identifiable
import ly.david.musicsearch.shared.domain.recording.RecordingFacet

interface ListensListRepository {
    fun observeListens(
        username: String,
        query: String,
        recordingId: String,
        stopPrepending: Boolean,
        stopAppending: Boolean,
        onReachedLatest: (Boolean) -> Unit,
        onReachedOldest: (Boolean) -> Unit,
    ): Flow<PagingData<Identifiable>>

    fun observeUnfilteredCountOfListensByUser(username: String): Flow<Long?>

    fun observeRecordingFacets(
        username: String,
        query: String,
    ): Flow<PagingData<RecordingFacet>>
}
