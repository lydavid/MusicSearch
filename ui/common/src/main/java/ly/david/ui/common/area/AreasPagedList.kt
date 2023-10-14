package ly.david.ui.common.area

import ly.david.musicsearch.data.core.listitem.AreaListItemModel
import ly.david.ui.common.paging.PagedList
import org.koin.core.annotation.Factory

@Factory
class AreasPagedList : PagedList<AreaListItemModel, AreaListItemModel>()
