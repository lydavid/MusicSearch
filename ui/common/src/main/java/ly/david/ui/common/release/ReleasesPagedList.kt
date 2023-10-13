package ly.david.ui.common.release

import ly.david.musicsearch.data.core.release.ReleaseForListItem
import ly.david.musicsearch.data.core.listitem.ReleaseListItemModel
import ly.david.ui.common.paging.PagedList
import org.koin.core.annotation.Factory

@Factory
class ReleasesPagedList : PagedList<ReleaseForListItem, ReleaseListItemModel>()
