package ly.david.musicsearch.domain.history

import androidx.paging.PagingSource
import ly.david.data.core.history.LookupHistory
import ly.david.data.core.history.LookupHistoryForListItem

interface LookupHistoryRepository {
    fun upsert(lookupHistory: LookupHistory)
    fun getAllLookupHistory(query: String, sortOption: HistorySortOption): PagingSource<Int, LookupHistoryForListItem>
    fun markHistoryAsDeleted(mbid: String)
    fun undoDeleteHistory(mbid: String)
    fun markAllHistoryAsDeleted()
    fun undoDeleteAllHistory()
    fun deleteHistory(mbid: String)
    fun deleteAllHistory()
}
