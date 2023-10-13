package ly.david.musicsearch.domain.listitem

import ly.david.data.musicbrainz.InstrumentMusicBrainzModel
import ly.david.musicsearch.data.core.listitem.InstrumentListItemModel
import lydavidmusicsearchdatadatabase.Instrument

internal fun InstrumentMusicBrainzModel.toInstrumentListItemModel() =
    InstrumentListItemModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        description = description,
        type = type,
    )

fun Instrument.toInstrumentListItemModel() =
    InstrumentListItemModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        description = description,
        type = type,
    )
