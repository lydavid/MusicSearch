package ly.david.musicsearch.shared.domain.listitem

import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType

data class SearchHistoryListItemModel(
    override val id: String,
    val query: String,
    val entityType: MusicBrainzEntityType,
) : ListItemModel
