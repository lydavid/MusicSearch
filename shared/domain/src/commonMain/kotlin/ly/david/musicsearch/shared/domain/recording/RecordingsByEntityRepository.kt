package ly.david.musicsearch.shared.domain.recording

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.RecordingListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

interface RecordingsByEntityRepository {
    fun observeRecordingsByEntity(
        entityId: String?,
        entity: MusicBrainzEntity?,
        listFilters: ListFilters,
    ): Flow<PagingData<RecordingListItemModel>>
}
