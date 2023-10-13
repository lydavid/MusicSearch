package ly.david.musicsearch.domain.listitem

import ly.david.musicsearch.data.core.Coordinates
import ly.david.data.musicbrainz.PlaceMusicBrainzModel
import ly.david.musicsearch.domain.common.LifeSpanUiModel
import ly.david.musicsearch.domain.common.toLifeSpanUiModel
import ly.david.musicsearch.domain.place.CoordinatesUiModel
import lydavidmusicsearchdatadatabase.Place

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
) : ly.david.musicsearch.data.core.Place, ListItemModel()

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
