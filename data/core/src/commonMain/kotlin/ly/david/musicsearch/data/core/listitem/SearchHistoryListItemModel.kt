package ly.david.musicsearch.data.core.listitem

import ly.david.musicsearch.data.core.network.MusicBrainzEntity

data class SearchHistoryListItemModel(
    override val id: String,
    val query: String,
    val entity: MusicBrainzEntity,
) : ListItemModel()
