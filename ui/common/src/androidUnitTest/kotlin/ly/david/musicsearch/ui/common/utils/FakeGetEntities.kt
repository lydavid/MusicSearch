package ly.david.musicsearch.ui.common.utils

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.list.GetEntities
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

class FakeGetEntities(private val listItems: List<ListItemModel>) : GetEntities {
    override fun invoke(
        entity: MusicBrainzEntity,
        browseMethod: BrowseMethod?,
        listFilters: ListFilters,
    ): Flow<PagingData<ListItemModel>> {
        return flowOf(PagingData.from(listItems))
    }
}
