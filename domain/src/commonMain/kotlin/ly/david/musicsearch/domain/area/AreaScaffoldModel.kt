package ly.david.musicsearch.domain.area

import ly.david.musicsearch.domain.common.LifeSpanUiModel
import ly.david.musicsearch.domain.listitem.RelationListItemModel
import ly.david.musicsearch.domain.listitem.toRelationListItemModel
import lydavidmusicsearchdatadatabase.Area
import lydavidmusicsearchdatadatabase.Relation

data class AreaScaffoldModel(
    override val id: String,
    override val name: String,
    override val sortName: String = "",
    override val disambiguation: String? = "",
    override val type: String? = "",
    override val lifeSpan: LifeSpanUiModel? = null,
    val countryCodes: List<String>? = null,
    val urls: List<RelationListItemModel> = listOf(),
) : ly.david.data.core.area.Area

internal fun Area.toAreaScaffoldModel(
    countryCodes: List<String>,
    urls: List<Relation>,
) = AreaScaffoldModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
    type = type,
    lifeSpan = LifeSpanUiModel(
        begin = begin,
        end = end,
        ended = ended,
    ),
    countryCodes = countryCodes,
    urls = urls.map { it.toRelationListItemModel() },
)
