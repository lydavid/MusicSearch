package ly.david.musicsearch.domain.listitem

import ly.david.musicsearch.data.core.LifeSpanUiModel
import ly.david.musicsearch.data.core.listitem.PlaceListItemModel
import ly.david.musicsearch.domain.place.CoordinatesUiModel
import lydavidmusicsearchdatadatabase.Place

fun Place.toPlaceListItemModel() = PlaceListItemModel(
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
