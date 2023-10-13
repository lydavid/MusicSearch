package ly.david.musicsearch.domain.listitem

import ly.david.musicsearch.data.core.history.SearchHistory
import ly.david.musicsearch.data.core.network.MusicBrainzEntity

data class SearchHistoryListItemModel(
    override val id: String,
    val query: String,
    val entity: MusicBrainzEntity,
) : ListItemModel()

fun SearchHistory.toSearchHistoryListItemModel() = SearchHistoryListItemModel(
    id = "${query}_$entity",
    query = query,
    entity = entity
)
