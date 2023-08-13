package ly.david.data.domain.listitem

import ly.david.data.Coordinates
import ly.david.data.LifeSpan
import ly.david.data.Place
import ly.david.data.network.PlaceMusicBrainzModel
import ly.david.data.room.place.PlaceRoomModel
import ly.david.data.room.place.PlaceWithAllData

data class PlaceListItemModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val address: String,
    override val type: String? = null,
//    override val typeId: String?,
    override val coordinates: Coordinates? = null,
    override val lifeSpan: LifeSpan? = null,

    val area: AreaListItemModel? = null,
) : Place, ListItemModel()

internal fun PlaceMusicBrainzModel.toPlaceListItemModel() =
    PlaceListItemModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        address = address,
        type = type,
        coordinates = coordinates,
        lifeSpan = lifeSpan,
        area = area?.toAreaListItemModel()
    )

fun PlaceRoomModel.toPlaceListItemModel() = PlaceListItemModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
    address = address,
    type = type,
    coordinates = coordinates,
    lifeSpan = lifeSpan,
    area = null
)

fun PlaceWithAllData.toPlaceListItemModel() = PlaceListItemModel(
    id = place.id,
    name = place.name,
    disambiguation = place.disambiguation,
    address = place.address,
    type = place.type,
    coordinates = place.coordinates,
    lifeSpan = place.lifeSpan,
    area = area?.toAreaListItemModel()
)
