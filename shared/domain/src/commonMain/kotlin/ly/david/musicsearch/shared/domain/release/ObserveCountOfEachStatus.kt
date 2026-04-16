package ly.david.musicsearch.shared.domain.release

import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.BrowseMethod

interface ObserveCountOfEachStatus {
    operator fun invoke(
        browseMethod: BrowseMethod,
    ): Flow<List<ReleaseStatusCount>>
}
