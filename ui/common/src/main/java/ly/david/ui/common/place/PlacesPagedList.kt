package ly.david.ui.common.place

import ly.david.musicsearch.domain.listitem.PlaceListItemModel
import ly.david.ui.common.paging.PagedList
import lydavidmusicsearchdatadatabase.Place
import org.koin.core.annotation.Factory

@Factory
class PlacesPagedList : PagedList<Place, PlaceListItemModel>()
