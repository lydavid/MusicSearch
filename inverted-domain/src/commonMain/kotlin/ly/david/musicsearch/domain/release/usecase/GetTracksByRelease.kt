package ly.david.musicsearch.domain.release.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.data.core.listitem.ListItemModel
import ly.david.musicsearch.domain.release.ReleaseRepository
import org.koin.core.annotation.Single

@Single
class GetTracksByRelease(
    private val releaseRepository: ReleaseRepository,
) {
    operator fun invoke(
        releaseId: String,
        query: String,
    ): Flow<PagingData<ListItemModel>> = releaseRepository.observeTracksByRelease(
        releaseId = releaseId,
        query = query,
    )
}
