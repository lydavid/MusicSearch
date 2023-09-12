package ly.david.ui.common.area

import ly.david.data.domain.listitem.AreaListItemModel
import ly.david.data.room.area.AreaRoomModel
import ly.david.ui.common.paging.PagedList
import org.koin.core.annotation.Factory

@Factory
class AreasPagedList : PagedList<AreaRoomModel, AreaListItemModel>()
