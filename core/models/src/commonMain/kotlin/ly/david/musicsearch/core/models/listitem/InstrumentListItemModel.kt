package ly.david.musicsearch.core.models.listitem

import ly.david.musicsearch.core.models.instrument.Instrument

data class InstrumentListItemModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val description: String? = null,
    override val type: String? = null,
) : Instrument, ListItemModel()
