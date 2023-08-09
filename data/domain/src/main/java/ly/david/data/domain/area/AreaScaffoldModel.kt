package ly.david.data.domain.area

import ly.david.data.Area
import ly.david.data.LifeSpan
import ly.david.data.domain.listitem.RelationListItemModel
import ly.david.data.domain.listitem.toRelationListItemModel
import ly.david.data.room.area.AreaWithAllData

data class AreaScaffoldModel(
    override val id: String,
    override val name: String,
    override val sortName: String = "",
    override val disambiguation: String? = "",
    override val type: String? = "",
    override val lifeSpan: LifeSpan? = null,
    val countryCodes: List<String>? = null,
    val urls: List<RelationListItemModel> = listOf(),
) : Area

internal fun AreaWithAllData.toAreaScaffoldModel() = AreaScaffoldModel(
    id = area.id,
    name = area.name,
    disambiguation = area.disambiguation,
    type = area.type,
    lifeSpan = area.lifeSpan,
    countryCodes = countryCodes.map { it.code },
    urls = urls.map { it.relation.toRelationListItemModel() },
)
