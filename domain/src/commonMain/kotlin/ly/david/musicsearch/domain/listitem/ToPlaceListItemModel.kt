package ly.david.musicsearch.domain.listitem

import ly.david.data.musicbrainz.PlaceMusicBrainzModel
import ly.david.musicsearch.data.core.listitem.PlaceListItemModel
import ly.david.musicsearch.data.core.LifeSpanUiModel
import ly.david.musicsearch.domain.common.toLifeSpanUiModel
import ly.david.musicsearch.domain.place.CoordinatesUiModel
import lydavidmusicsearchdatadatabase.Place

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
