package ly.david.musicsearch.shared.domain.recording

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.RecordingListItemModel

interface RecordingsByEntityRepository {
    fun observeRecordingsByEntity(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): Flow<PagingData<RecordingListItemModel>>
}
