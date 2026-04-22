package ly.david.musicsearch.shared.domain.list

import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.release.ReleaseStatus

interface ObserveLocalCount {
    operator fun invoke(
        browseEntity: MusicBrainzEntityType,
        browseMethod: BrowseMethod?,
        query: String,
        showReleaseStatuses: Set<ReleaseStatus>,
    ): Flow<Int>
}
