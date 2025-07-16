package ly.david.musicsearch.shared.domain.releasegroup

import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.BrowseMethod

interface ObserveCountOfEachAlbumType {
    operator fun invoke(
        browseMethod: BrowseMethod,
    ): Flow<List<ReleaseGroupTypeCount>>
}
