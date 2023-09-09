package ly.david.ui.common.release

import ly.david.data.domain.listitem.ReleaseListItemModel
import ly.david.data.room.release.ReleaseForListItem
import ly.david.ui.common.paging.PagedList
import org.koin.core.annotation.Factory

@Factory
class ReleasesPagedList: PagedList<ReleaseForListItem, ReleaseListItemModel>()
