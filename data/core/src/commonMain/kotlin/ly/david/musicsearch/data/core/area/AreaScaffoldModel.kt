package ly.david.musicsearch.data.core.area

import ly.david.musicsearch.data.core.LifeSpanUiModel
import ly.david.musicsearch.data.core.listitem.RelationListItemModel

data class AreaScaffoldModel(
    override val id: String,
    override val name: String,
    override val sortName: String = "",
    override val disambiguation: String? = "",
    override val type: String? = "",
    override val lifeSpan: LifeSpanUiModel? = null,
    val countryCodes: List<String>? = null,
    val urls: List<RelationListItemModel> = listOf(),
) : Area

//fun Area.toAreaScaffoldModel(
//    countryCodes: List<String>,
//    urls: List<RelationListItemModel>,
//) = AreaScaffoldModel(
//    id = id,
//    name = name,
//    disambiguation = disambiguation,
//    type = type,
//    lifeSpan = LifeSpanUiModel(
//        begin = begin,
//        end = end,
//        ended = ended,
//    ),
//    countryCodes = countryCodes,
//    urls = urls,
//)
