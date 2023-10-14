package ly.david.ui.common.artist

import ly.david.musicsearch.data.core.listitem.ArtistListItemModel
import ly.david.ui.common.paging.PagedList
import org.koin.core.annotation.Factory

@Factory
class ArtistsPagedList : PagedList<ArtistListItemModel, ArtistListItemModel>()
