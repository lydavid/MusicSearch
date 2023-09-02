package ly.david.data.domain.listitem

import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.room.collection.CollectionWithEntities

data class CollectionListItemModel(
    override val id: String,
    val isRemote: Boolean,
    val name: String,
    val description: String = "",
    val entity: MusicBrainzEntity,
    val entityCount: Int = 0,
    val entityIds: List<String> = listOf(),
) : ListItemModel()

fun CollectionWithEntities.toCollectionListItemModel() =
    CollectionListItemModel(
        id = collection.id,
        isRemote = collection.isRemote,
        name = collection.name,
        entity = collection.entity,
        entityCount = collection.entityCount,
        entityIds = entities.map { it.entityId }
    )
