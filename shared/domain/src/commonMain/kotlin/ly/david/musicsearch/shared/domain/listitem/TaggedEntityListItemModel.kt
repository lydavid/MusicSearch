package ly.david.musicsearch.shared.domain.listitem

import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType

data class TaggedEntityListItemModel(
    override val id: String,
    val entityType: MusicBrainzEntityType,
    val entityName: String,
    val disambiguation: String = "",
) : ListItemModel
