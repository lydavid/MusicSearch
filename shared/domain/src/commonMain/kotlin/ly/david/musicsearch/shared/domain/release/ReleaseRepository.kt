package ly.david.musicsearch.shared.domain.release

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant
import ly.david.musicsearch.shared.domain.details.ReleaseDetailsModel
import ly.david.musicsearch.shared.domain.listitem.ListItemModel

interface ReleaseRepository {
    suspend fun lookupRelease(
        releaseId: String,
        forceRefresh: Boolean,
        lastUpdated: Instant,
    ): ReleaseDetailsModel

    fun observeTracksByRelease(
        releaseId: String,
        query: String,
        lastUpdated: Instant,
    ): Flow<PagingData<ListItemModel>>
}
