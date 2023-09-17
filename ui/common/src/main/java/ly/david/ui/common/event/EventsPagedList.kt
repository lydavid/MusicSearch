package ly.david.ui.common.event

import ly.david.data.domain.listitem.EventListItemModel
import ly.david.ui.common.paging.PagedList
import lydavidmusicsearchdatadatabase.Event
import org.koin.core.annotation.Factory

@Factory
class EventsPagedList : PagedList<Event, EventListItemModel>()
