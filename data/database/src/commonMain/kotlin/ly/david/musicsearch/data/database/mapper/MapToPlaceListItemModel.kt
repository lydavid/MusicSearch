package ly.david.musicsearch.data.database.mapper

import ly.david.musicsearch.data.core.LifeSpanUiModel
import ly.david.musicsearch.data.core.listitem.PlaceListItemModel
import ly.david.musicsearch.data.core.place.CoordinatesUiModel

fun mapToPlaceListItemModel(
    id: String,
    name: String,
    disambiguation: String?,
    address: String,
    type: String?,
    longitude: Double?,
    latitude: Double?,
    begin: String?,
    end: String?,
    ended: Boolean?,
) = PlaceListItemModel(
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
    area = null,
)
