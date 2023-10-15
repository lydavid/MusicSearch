package ly.david.ui.common.work

import ly.david.musicsearch.core.models.listitem.WorkListItemModel
import ly.david.ui.common.paging.PagedList
import org.koin.core.annotation.Factory

@Factory
class WorksPagedList : PagedList<WorkListItemModel, WorkListItemModel>()
