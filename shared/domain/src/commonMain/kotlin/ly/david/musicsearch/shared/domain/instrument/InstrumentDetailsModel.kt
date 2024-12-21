package ly.david.musicsearch.shared.domain.instrument

import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.wikimedia.WikipediaExtract

data class InstrumentDetailsModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val description: String? = null,
    override val type: String? = null,
    val wikipediaExtract: WikipediaExtract = WikipediaExtract(),
    val urls: List<RelationListItemModel> = listOf(),
) : Instrument
