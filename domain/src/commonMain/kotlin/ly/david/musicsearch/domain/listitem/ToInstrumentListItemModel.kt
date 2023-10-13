package ly.david.musicsearch.domain.listitem

import ly.david.musicsearch.data.core.listitem.InstrumentListItemModel
import lydavidmusicsearchdatadatabase.Instrument

fun Instrument.toInstrumentListItemModel() =
    InstrumentListItemModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        description = description,
        type = type,
    )
