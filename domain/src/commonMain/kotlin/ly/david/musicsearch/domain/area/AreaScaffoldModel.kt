package ly.david.musicsearch.domain.area

import ly.david.musicsearch.data.core.LifeSpanUiModel
import ly.david.musicsearch.data.core.listitem.RelationListItemModel
import lydavidmusicsearchdatadatabase.Area

data class AreaScaffoldModel(
    override val id: String,
    override val name: String,
    override val sortName: String = "",
    override val disambiguation: String? = "",
    override val type: String? = "",
    override val lifeSpan: LifeSpanUiModel? = null,
    val countryCodes: List<String>? = null,
    val urls: List<RelationListItemModel> = listOf(),
) : ly.david.musicsearch.data.core.area.Area

fun Area.toAreaScaffoldModel(
    countryCodes: List<String>,
    urls: List<RelationListItemModel>,
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
    urls = urls,
)
