package ly.david.musicsearch.core.models.instrument

import ly.david.musicsearch.core.models.listitem.RelationListItemModel

data class InstrumentScaffoldModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val description: String? = null,
    override val type: String? = null,
    val urls: List<RelationListItemModel> = listOf(),
) : Instrument
