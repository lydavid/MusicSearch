package ly.david.musicsearch.ui.common.utils

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.list.EntitiesListRepository
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

class FakeEntitiesListRepository(private val listItems: List<ListItemModel>) : EntitiesListRepository {
    override fun observeEntities(
        entity: MusicBrainzEntity,
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): Flow<PagingData<ListItemModel>> {
        error("not used")
    }

    override fun observeLocalCount(
        browseEntity: MusicBrainzEntity,
        browseMethod: BrowseMethod?,
    ): Flow<Int> {
        return flowOf(listItems.size)
    }
}
