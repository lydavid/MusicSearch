package ly.david.ui.common.place

import ly.david.data.domain.listitem.PlaceListItemModel
import ly.david.data.room.place.PlaceRoomModel
import ly.david.ui.common.paging.PagedList
import org.koin.core.annotation.Factory

@Factory
class PlacesPagedList: PagedList<PlaceRoomModel, PlaceListItemModel>()
