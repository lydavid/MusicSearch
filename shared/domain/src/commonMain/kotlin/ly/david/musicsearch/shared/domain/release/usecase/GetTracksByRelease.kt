package ly.david.musicsearch.shared.domain.release.usecase

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.release.ReleaseRepository

class GetTracksByRelease(
    private val releaseRepository: ReleaseRepository,
) {
    operator fun invoke(
        releaseId: String,
        query: String,
        collapsedMediumIds: Set<Long>,
    ): Flow<PagingData<ListItemModel>> = releaseRepository.observeTracksByRelease(
        releaseId = releaseId,
        query = query,
        collapsedMediumIds = collapsedMediumIds,
    )
}
