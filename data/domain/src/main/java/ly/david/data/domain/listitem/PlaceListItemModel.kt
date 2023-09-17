package ly.david.data.domain.listitem

import ly.david.data.core.Coordinates
import ly.david.data.domain.common.LifeSpanUiModel
import ly.david.data.domain.common.toLifeSpanUiModel
import ly.david.data.domain.place.CoordinatesUiModel
import ly.david.data.musicbrainz.PlaceMusicBrainzModel
import ly.david.data.room.place.PlaceRoomModel

data class PlaceListItemModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val address: String,
    override val type: String? = null,
//    override val typeId: String?,
    override val coordinates: Coordinates? = null,
    override val lifeSpan: LifeSpanUiModel? = null,

    val area: AreaListItemModel? = null,
) : ly.david.data.core.Place, ListItemModel()

internal fun PlaceMusicBrainzModel.toPlaceListItemModel() =
    PlaceListItemModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        address = address,
        type = type,
        coordinates = coordinates,
        lifeSpan = lifeSpan?.toLifeSpanUiModel(),
        area = area?.toAreaListItemModel()
    )

fun lydavidmusicsearchdatadatabase.Place.toPlaceListItemModel() = PlaceListItemModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
    address = address,
    type = type,
    coordinates = CoordinatesUiModel(
        longitude = longitude,
        latitude = latitude,
    ),
    lifeSpan = LifeSpanUiModel(
        begin = begin,
        end = end,
        ended = ended,
    ),
    area = null
)

fun PlaceRoomModel.toPlaceListItemModel() = PlaceListItemModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
    address = address,
    type = type,
    coordinates = coordinates,
    lifeSpan = lifeSpan?.toLifeSpanUiModel(),
    area = null
)
