package ly.david.ui.common.event

import ly.david.musicsearch.core.models.listitem.EventListItemModel
import ly.david.ui.common.paging.PagedList
import org.koin.core.annotation.Factory

@Factory
class EventsPagedList : PagedList<EventListItemModel, EventListItemModel>()
