package ly.david.data.domain.listitem

import ly.david.data.network.MusicBrainzResource
import ly.david.data.room.history.search.SearchHistoryRoomModel

data class SearchHistoryListItemModel(
    override val id: String,
    val query: String,
    val entity: MusicBrainzResource,
) : ListItemModel()

fun SearchHistoryRoomModel.toSearchHistoryListItemModel() = SearchHistoryListItemModel(
    id = id,
    query = query,
    entity = entity
)
