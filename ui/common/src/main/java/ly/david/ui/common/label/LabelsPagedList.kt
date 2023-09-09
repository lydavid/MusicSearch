package ly.david.ui.common.label

import ly.david.data.domain.listitem.LabelListItemModel
import ly.david.data.room.label.LabelRoomModel
import ly.david.ui.common.paging.PagedList
import org.koin.core.annotation.Factory

@Factory
class LabelsPagedList: PagedList<LabelRoomModel, LabelListItemModel>()
