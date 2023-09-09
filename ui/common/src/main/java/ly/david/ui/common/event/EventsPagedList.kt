package ly.david.ui.common.event

import ly.david.data.domain.listitem.EventListItemModel
import ly.david.data.room.event.EventRoomModel
import ly.david.ui.common.paging.PagedList
import org.koin.core.annotation.Factory

@Factory
class EventsPagedList: PagedList<EventRoomModel, EventListItemModel>()
