package ly.david.musicsearch.data.core.listitem

import ly.david.musicsearch.data.core.network.MusicBrainzEntity

data class CollectionListItemModel(
    override val id: String,
    val isRemote: Boolean,
    val name: String,
    val description: String = "",
    val entity: MusicBrainzEntity,
    val entityCount: Int = 0,
    val entityIds: List<String> = listOf(),
) : ListItemModel()
