package ly.david.data.domain

import ly.david.data.network.MusicBrainzResource
import ly.david.data.persistence.collection.CollectionRoomModel

data class CollectionListItemModel(
    override val id: String,
    val name: String,
    val entity: MusicBrainzResource
) : ListItemModel()

fun CollectionRoomModel.toCollectionListItemModel() =
    CollectionListItemModel(
        id = id.toString(),
        name = name,
        entity = entity
    )
