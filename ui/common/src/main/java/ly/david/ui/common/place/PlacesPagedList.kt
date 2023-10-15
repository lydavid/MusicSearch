package ly.david.ui.common.place

import ly.david.musicsearch.core.models.listitem.PlaceListItemModel
import ly.david.ui.common.paging.PagedList
import org.koin.core.annotation.Factory

@Factory
class PlacesPagedList : PagedList<PlaceListItemModel>()
