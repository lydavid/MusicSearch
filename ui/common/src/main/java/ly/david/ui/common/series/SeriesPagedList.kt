package ly.david.ui.common.series

import ly.david.data.domain.listitem.SeriesListItemModel
import ly.david.data.room.series.SeriesRoomModel
import ly.david.ui.common.paging.PagedList
import org.koin.core.annotation.Factory

@Factory
class SeriesPagedList : PagedList<SeriesRoomModel, SeriesListItemModel>()
