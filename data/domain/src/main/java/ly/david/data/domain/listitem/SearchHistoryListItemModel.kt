package ly.david.data.domain.listitem

import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.room.history.search.SearchHistoryRoomModel

data class SearchHistoryListItemModel(
    override val id: String,
    val query: String,
    val entity: MusicBrainzEntity,
) : ListItemModel()

fun SearchHistoryRoomModel.toSearchHistoryListItemModel() = SearchHistoryListItemModel(
    id = id,
    query = query,
    entity = entity
)
