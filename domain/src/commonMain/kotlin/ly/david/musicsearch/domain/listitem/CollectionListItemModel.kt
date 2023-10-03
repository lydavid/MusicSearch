package ly.david.musicsearch.domain.listitem

import ly.david.data.core.network.MusicBrainzEntity
import lydavidmusicsearchdatadatabase.Collection

data class CollectionListItemModel(
    override val id: String,
    val isRemote: Boolean,
    val name: String,
    val description: String = "",
    val entity: MusicBrainzEntity,
    val entityCount: Int = 0,
    val entityIds: List<String> = listOf(),
) : ListItemModel()

fun Collection.toCollectionListItemModel() =
    CollectionListItemModel(
        id = id,
        isRemote = is_remote,
        name = name,
        entity = entity,
        entityCount = entity_count,
    )
