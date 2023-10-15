package ly.david.musicsearch.data.core.listitem

import ly.david.musicsearch.data.core.instrument.Instrument

data class InstrumentListItemModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val description: String? = null,
    override val type: String? = null,
) : Instrument, ListItemModel()
