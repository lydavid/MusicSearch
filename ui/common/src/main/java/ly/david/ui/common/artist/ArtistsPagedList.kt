package ly.david.ui.common.artist

import ly.david.data.domain.listitem.ArtistListItemModel
import ly.david.ui.common.paging.PagedList
import lydavidmusicsearchdatadatabase.Artist
import org.koin.core.annotation.Factory

@Factory
class ArtistsPagedList : PagedList<Artist, ArtistListItemModel>()
