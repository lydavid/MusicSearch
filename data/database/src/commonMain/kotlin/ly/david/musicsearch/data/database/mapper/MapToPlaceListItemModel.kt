package ly.david.musicsearch.data.database.mapper

import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.listitem.PlaceListItemModel
import ly.david.musicsearch.shared.domain.place.CoordinatesUiModel

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
    visited: Boolean?,
    collected: Boolean?,
    aliasNames: String?,
    aliasLocales: String?,
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
        begin = begin.orEmpty(),
        end = end.orEmpty(),
        ended = ended == true,
    ),
    visited = visited == true,
    collected = collected == true,
    aliases = combineToAliases(aliasNames, aliasLocales),
)
