package ly.david.data.domain

import ly.david.data.Coordinates
import ly.david.data.LifeSpan
import ly.david.data.Place
import ly.david.data.network.PlaceMusicBrainzModel
import ly.david.data.persistence.place.PlaceRoomModel

data class PlaceUiModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val address: String,
    override val type: String? = null,
//    override val typeId: String?,
    override val coordinates: Coordinates? = null,
    override val lifeSpan: LifeSpan? = null,

    val area: AreaCardModel? = null,
) : Place, UiModel()

internal fun PlaceMusicBrainzModel.toPlaceUiModel() =
    PlaceUiModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        address = address,
        type = type,
        coordinates = coordinates,
        lifeSpan = lifeSpan,
        area = area?.toCardModel()
    )

internal fun PlaceRoomModel.toPlaceUiModel(
    area: AreaCardModel? = null
) =
    PlaceUiModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        address = address,
        type = type,
        coordinates = coordinates,
        lifeSpan = lifeSpan,
        area = area
    )
