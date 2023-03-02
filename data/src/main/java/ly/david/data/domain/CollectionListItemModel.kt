package ly.david.data.domain

import ly.david.data.network.MusicBrainzResource
import ly.david.data.persistence.collection.CollectionWithEntities

data class CollectionListItemModel(
    override val id: String,
    val name: String,
    val description: String = "",
    val entity: MusicBrainzResource,
    val entityIds: List<String> = listOf()
) : ListItemModel()

fun CollectionWithEntities.toCollectionListItemModel() =
    CollectionListItemModel(
        id = collection.id.toString(),
        name = collection.name,
        entity = collection.entity,
        entityIds = entities.map { it.entityId }
    )
