package ly.david.ui.common.work

import ly.david.data.domain.listitem.WorkListItemModel
import ly.david.data.room.work.WorkRoomModel
import ly.david.ui.common.paging.PagedList
import org.koin.core.annotation.Factory

@Factory
class WorksPagedList : PagedList<WorkRoomModel, WorkListItemModel>()
