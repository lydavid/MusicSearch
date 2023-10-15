package ly.david.ui.common.release

import ly.david.musicsearch.core.models.listitem.ReleaseListItemModel
import ly.david.ui.common.paging.PagedList
import org.koin.core.annotation.Factory

@Factory
class ReleasesPagedList : PagedList<ReleaseListItemModel, ReleaseListItemModel>()
