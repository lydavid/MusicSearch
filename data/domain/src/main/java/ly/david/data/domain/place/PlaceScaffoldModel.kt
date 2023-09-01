package ly.david.data.domain.place

import ly.david.data.CoordinatesUiModel
import ly.david.data.LifeSpanUiModel
import ly.david.data.Place
import ly.david.data.domain.listitem.AreaListItemModel
import ly.david.data.domain.listitem.RelationListItemModel
import ly.david.data.domain.listitem.toAreaListItemModel
import ly.david.data.domain.listitem.toRelationListItemModel
import ly.david.data.room.place.PlaceWithAllData
import ly.david.data.toCoordinatesUiModel
import ly.david.data.toLifeSpanUiModel

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
) : Place

internal fun PlaceWithAllData.toPlaceScaffoldModel() = PlaceScaffoldModel(
    id = place.id,
    name = place.name,
    disambiguation = place.disambiguation,
    address = place.address,
    type = place.type,
    coordinates = place.coordinates?.toCoordinatesUiModel(),
    lifeSpan = place.lifeSpan?.toLifeSpanUiModel(),
    area = area?.toAreaListItemModel(),
    urls = urls.map { it.relation.toRelationListItemModel() },
)
