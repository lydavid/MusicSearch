package ly.david.musicsearch.shared.domain.release

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.details.ReleaseDetailsModel
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.sort.TrackSortOption
import kotlin.time.Instant

interface ReleaseRepository {
    suspend fun lookupEntity(
        entityId: String,
        forceRefresh: Boolean,
        lastUpdated: Instant,
    ): ReleaseDetailsModel

    fun observeTracksByRelease(
        releaseId: String,
        query: String,
        lastUpdated: Instant,
        sortOption: TrackSortOption,
    ): Flow<PagingData<ListItemModel>>
}
