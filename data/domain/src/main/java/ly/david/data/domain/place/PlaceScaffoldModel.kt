package ly.david.data.domain.place

import ly.david.data.core.Place
import ly.david.data.domain.common.LifeSpanUiModel
import ly.david.data.domain.listitem.AreaListItemModel
import ly.david.data.domain.listitem.RelationListItemModel
import ly.david.data.domain.listitem.toAreaListItemModel
import ly.david.data.domain.listitem.toRelationListItemModel
import lydavidmusicsearchdatadatabase.Mb_area
import lydavidmusicsearchdatadatabase.Mb_place
import lydavidmusicsearchdatadatabase.Mb_relation

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

internal fun Mb_place.toPlaceScaffoldModel(
    area: Mb_area?,
    urls: List<Mb_relation>,
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
