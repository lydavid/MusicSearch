package ly.david.musicsearch.shared.domain.listitem

import ly.david.musicsearch.shared.domain.instrument.Instrument

data class InstrumentListItemModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val description: String? = null,
    override val type: String? = null,
) : Instrument, ListItemModel()
