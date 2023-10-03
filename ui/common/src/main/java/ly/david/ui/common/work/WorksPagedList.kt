package ly.david.ui.common.work

import ly.david.musicsearch.domain.listitem.WorkListItemModel
import ly.david.ui.common.paging.PagedList
import lydavidmusicsearchdatadatabase.Work
import org.koin.core.annotation.Factory

@Factory
class WorksPagedList : PagedList<Work, WorkListItemModel>()
