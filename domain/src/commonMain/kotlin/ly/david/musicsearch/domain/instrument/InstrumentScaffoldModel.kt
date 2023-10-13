package ly.david.musicsearch.domain.instrument

import ly.david.musicsearch.data.core.listitem.RelationListItemModel
import lydavidmusicsearchdatadatabase.Instrument

data class InstrumentScaffoldModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val description: String? = null,
    override val type: String? = null,
    val urls: List<RelationListItemModel> = listOf(),
) : ly.david.musicsearch.data.core.Instrument

internal fun Instrument.toInstrumentListItemModel(
    urls: List<RelationListItemModel>,
) = InstrumentScaffoldModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
    description = description,
    type = type,
    urls = urls,
)
