package ly.david.musicsearch.ui.common.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.list.ObserveLocalCount
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType

class FakeObserveLocalCount(private val listItems: List<ListItemModel>) : ObserveLocalCount {
    override operator fun invoke(
        browseEntity: MusicBrainzEntityType,
        browseMethod: BrowseMethod?,
    ): Flow<Int> {
        return flowOf(listItems.size)
    }
}
