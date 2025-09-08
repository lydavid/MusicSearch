package ly.david.musicsearch.shared.domain.release.usecase

import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.release.ReleaseRepository
import kotlin.time.Clock

interface GetTracksByRelease {
    operator fun invoke(
        releaseId: String,
        mostListenedTrackCount: Long,
        query: String,
    ): Flow<PagingData<ListItemModel>>
}

class GetTracksByReleaseImpl(
    private val releaseRepository: ReleaseRepository,
    private val coroutineScope: CoroutineScope,
) : GetTracksByRelease {
    override operator fun invoke(
        releaseId: String,
        mostListenedTrackCount: Long,
        query: String,
    ): Flow<PagingData<ListItemModel>> {
        return if (releaseId.isEmpty()) {
            emptyFlow()
        } else {
            releaseRepository.observeTracksByRelease(
                releaseId = releaseId,
                mostListenedTrackCount = mostListenedTrackCount,
                query = query,
                lastUpdated = Clock.System.now(),
            )
                .distinctUntilChanged()
                .cachedIn(scope = coroutineScope)
        }
    }
}
