package ly.david.musicsearch.core.models.listitem

import ly.david.musicsearch.core.models.network.MusicBrainzEntity

data class CollectionListItemModel(
    override val id: String,
    val isRemote: Boolean,
    val name: String,
    val description: String = "",
    val entity: MusicBrainzEntity,
    val entityCount: Int = 0,
) : ListItemModel()
