package ly.david.musicsearch.domain.listitem

import ly.david.data.musicbrainz.InstrumentMusicBrainzModel
import lydavidmusicsearchdatadatabase.Instrument

data class InstrumentListItemModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val description: String? = null,
    override val type: String? = null,
) : ly.david.musicsearch.data.core.Instrument, ListItemModel()

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
