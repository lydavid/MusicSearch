package ly.david.data.repository

import androidx.paging.PagingSource
import ly.david.data.persistence.recording.RecordingForListItem

interface RecordingsListRepository {
    suspend fun browseRecordingsAndStore(resourceId: String, nextOffset: Int): Int
    suspend fun getRemoteRecordingsCountByResource(resourceId: String): Int?
    suspend fun getLocalRecordingsCountByResource(resourceId: String): Int
    suspend fun deleteRecordingsByResource(resourceId: String)
    fun getRecordingsPagingSource(resourceId: String, query: String): PagingSource<Int, RecordingForListItem>
}
