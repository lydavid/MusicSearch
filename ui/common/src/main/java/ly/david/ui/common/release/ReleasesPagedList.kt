package ly.david.ui.common.release

import ly.david.data.core.release.ReleaseForListItem
import ly.david.musicsearch.domain.listitem.ReleaseListItemModel
import ly.david.ui.common.paging.PagedList
import org.koin.core.annotation.Factory

@Factory
class ReleasesPagedList : PagedList<ReleaseForListItem, ReleaseListItemModel>()
