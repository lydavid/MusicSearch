package ly.david.musicsearch.shared.domain.event

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.EventListItemModel

interface EventsListRepository {
    fun observeEvents(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): Flow<PagingData<EventListItemModel>>

    fun observeCountOfEvents(browseMethod: BrowseMethod?): Flow<Int>
}
