package ly.david.musicsearch.domain.history

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.data.core.history.HistorySortOption
import ly.david.musicsearch.data.core.history.LookupHistory
import ly.david.musicsearch.data.core.listitem.ListItemModel

interface LookupHistoryRepository {
    fun upsert(lookupHistory: LookupHistory)
    fun observeAllLookupHistory(query: String, sortOption: HistorySortOption): Flow<PagingData<ListItemModel>>
    fun markHistoryAsDeleted(mbid: String)
    fun undoDeleteHistory(mbid: String)
    fun markAllHistoryAsDeleted()
    fun undoDeleteAllHistory()
    fun deleteHistory(mbid: String)
    fun deleteAllHistory()
}
