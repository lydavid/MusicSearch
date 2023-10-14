package ly.david.musicsearch.domain.release

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.data.core.listitem.ListItemModel
import ly.david.musicsearch.data.core.release.ReleaseScaffoldModel

interface ReleaseRepository {
    suspend fun lookupRelease(releaseId: String): ReleaseScaffoldModel

    fun observeTracksByRelease(
        releaseId: String,
        query: String,
    ): Flow<PagingData<ListItemModel>>
}
