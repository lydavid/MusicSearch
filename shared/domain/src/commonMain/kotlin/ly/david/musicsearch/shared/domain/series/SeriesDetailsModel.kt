package ly.david.musicsearch.shared.domain.series

import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.wikimedia.WikipediaExtract

data class SeriesDetailsModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
    val wikipediaExtract: WikipediaExtract = WikipediaExtract(),
    val urls: List<RelationListItemModel> = listOf(),
) : Series
