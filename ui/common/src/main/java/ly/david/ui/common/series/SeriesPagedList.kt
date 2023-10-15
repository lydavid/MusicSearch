package ly.david.ui.common.series

import ly.david.musicsearch.core.models.listitem.SeriesListItemModel
import ly.david.ui.common.paging.PagedList
import org.koin.core.annotation.Factory

@Factory
class SeriesPagedList : PagedList<SeriesListItemModel, SeriesListItemModel>()
