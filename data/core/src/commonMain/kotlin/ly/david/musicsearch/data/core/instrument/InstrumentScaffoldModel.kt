package ly.david.musicsearch.data.core.instrument

import ly.david.musicsearch.data.core.Instrument
import ly.david.musicsearch.data.core.listitem.RelationListItemModel

data class InstrumentScaffoldModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val description: String? = null,
    override val type: String? = null,
    val urls: List<RelationListItemModel> = listOf(),
) : Instrument
