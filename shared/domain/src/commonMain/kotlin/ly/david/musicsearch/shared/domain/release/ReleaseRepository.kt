package ly.david.musicsearch.shared.domain.release

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.core.models.listitem.ListItemModel
import ly.david.musicsearch.core.models.release.ReleaseDetailsModel

interface ReleaseRepository {
    suspend fun lookupRelease(releaseId: String): ReleaseDetailsModel

    fun observeTracksByRelease(
        releaseId: String,
        query: String,
    ): Flow<PagingData<ListItemModel>>
}
