package ly.david.data.domain.place

import ly.david.data.domain.common.LifeSpanUiModel
import ly.david.data.domain.listitem.AreaListItemModel
import ly.david.data.domain.listitem.RelationListItemModel
import ly.david.data.domain.listitem.toAreaListItemModel
import ly.david.data.domain.listitem.toRelationListItemModel
import lydavidmusicsearchdatadatabase.Area
import lydavidmusicsearchdatadatabase.Place
import lydavidmusicsearchdatadatabase.Relation

data class PlaceScaffoldModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val address: String,
    override val type: String? = null,
    override val coordinates: CoordinatesUiModel? = null,
    override val lifeSpan: LifeSpanUiModel? = null,
    val area: AreaListItemModel? = null,
    val urls: List<RelationListItemModel> = listOf(),
) : ly.david.data.core.Place

internal fun Place.toPlaceScaffoldModel(
    area: Area?,
    urls: List<Relation>,
) = PlaceScaffoldModel(
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
    area = area?.toAreaListItemModel(),
    urls = urls.map { it.toRelationListItemModel() },
)
