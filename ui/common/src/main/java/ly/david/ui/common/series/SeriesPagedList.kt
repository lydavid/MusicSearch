package ly.david.ui.common.series

import ly.david.musicsearch.data.core.listitem.SeriesListItemModel
import ly.david.ui.common.paging.PagedList
import org.koin.core.annotation.Factory

@Factory
class SeriesPagedList : PagedList<SeriesListItemModel, SeriesListItemModel>()
