package ly.david.musicsearch.core.models.listitem

import ly.david.musicsearch.core.models.network.MusicBrainzEntity

data class SearchHistoryListItemModel(
    override val id: String,
    val query: String,
    val entity: MusicBrainzEntity,
) : ListItemModel()
