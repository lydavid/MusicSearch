package ly.david.ui.common.event

import ly.david.musicsearch.data.core.listitem.EventListItemModel
import ly.david.ui.common.paging.PagedList
import org.koin.core.annotation.Factory

@Factory
class EventsPagedList : PagedList<EventListItemModel, EventListItemModel>()
