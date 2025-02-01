package ly.david.musicsearch.shared.domain.release.usecase

import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.release.ReleaseRepository

class GetTracksByRelease(
    private val releaseRepository: ReleaseRepository,
    private val coroutineScope: CoroutineScope,
) {
    operator fun invoke(
        releaseId: String,
        query: String,
    ): Flow<PagingData<ListItemModel>> {
        return if (releaseId.isEmpty()) {
            emptyFlow()
        } else {
            releaseRepository.observeTracksByRelease(
                releaseId = releaseId,
                query = query,
            )
                .distinctUntilChanged()
                .cachedIn(scope = coroutineScope)
        }
    }
}
