package ly.david.data.domain.instrument

import ly.david.data.core.Instrument
import ly.david.data.domain.listitem.RelationListItemModel
import ly.david.data.domain.listitem.toRelationListItemModel
import ly.david.data.room.instrument.InstrumentWithAllData

data class InstrumentScaffoldModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val description: String? = null,
    override val type: String? = null,
    val urls: List<RelationListItemModel> = listOf(),
) : Instrument

internal fun InstrumentWithAllData.toInstrumentListItemModel() =
    InstrumentScaffoldModel(
        id = instrument.id,
        name = instrument.name,
        disambiguation = instrument.disambiguation,
        description = instrument.description,
        type = instrument.type,
        urls = urls.map { it.relation.toRelationListItemModel() },
    )
