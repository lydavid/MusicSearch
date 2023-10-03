package ly.david.ui.common.series

import ly.david.musicsearch.domain.listitem.SeriesListItemModel
import ly.david.ui.common.paging.PagedList
import lydavidmusicsearchdatadatabase.Series
import org.koin.core.annotation.Factory

@Factory
class SeriesPagedList : PagedList<Series, SeriesListItemModel>()
