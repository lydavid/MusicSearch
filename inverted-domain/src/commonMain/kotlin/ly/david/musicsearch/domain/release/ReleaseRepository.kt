package ly.david.musicsearch.domain.release

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.core.models.listitem.ListItemModel
import ly.david.musicsearch.core.models.release.ReleaseScaffoldModel

interface ReleaseRepository {
    suspend fun lookupRelease(releaseId: String): ReleaseScaffoldModel

    fun observeTracksByRelease(
        releaseId: String,
        query: String,
    ): Flow<PagingData<ListItemModel>>
}
