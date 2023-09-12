package ly.david.ui.common.artist

import ly.david.data.domain.listitem.ArtistListItemModel
import ly.david.data.room.artist.ArtistRoomModel
import ly.david.ui.common.paging.PagedList
import org.koin.core.annotation.Factory

@Factory
class ArtistsPagedList : PagedList<ArtistRoomModel, ArtistListItemModel>()
