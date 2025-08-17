package ly.david.musicsearch.shared.domain.list

import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType

interface ObserveCollectedCount {
    operator fun invoke(
        browseEntity: MusicBrainzEntityType,
        browseMethod: BrowseMethod?,
    ): Flow<Int>
}
