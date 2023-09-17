package ly.david.data.domain.instrument

import ly.david.data.domain.listitem.RelationListItemModel
import ly.david.data.domain.listitem.toRelationListItemModel
import lydavidmusicsearchdatadatabase.Instrument
import lydavidmusicsearchdatadatabase.Mb_relation

data class InstrumentScaffoldModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val description: String? = null,
    override val type: String? = null,
    val urls: List<RelationListItemModel> = listOf(),
) : ly.david.data.core.Instrument

internal fun Instrument.toInstrumentListItemModel(urls: List<Mb_relation>) =
    InstrumentScaffoldModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        description = description,
        type = type,
        urls = urls.map { it.toRelationListItemModel() },
    )
