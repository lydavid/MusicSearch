package ly.david.musicsearch.shared.domain.area

import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.wikimedia.WikipediaExtract

data class AreaDetailsModel(
    override val id: String,
    override val name: String,
    override val sortName: String = "",
    override val disambiguation: String? = null,
    override val type: String? = "",
    override val lifeSpan: LifeSpanUiModel = LifeSpanUiModel(),
    val countryCodes: List<String> = listOf(),
    val wikipediaExtract: WikipediaExtract = WikipediaExtract(),
    val urls: List<RelationListItemModel> = listOf(),
) : Area
