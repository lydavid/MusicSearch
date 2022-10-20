package ly.david.data.domain

import ly.david.data.Instrument
import ly.david.data.network.InstrumentMusicBrainzModel
import ly.david.data.persistence.instrument.InstrumentRoomModel

data class InstrumentUiModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val description: String? = null,
    override val type: String? = null,
) : Instrument, UiModel()

internal fun InstrumentMusicBrainzModel.toInstrumentUiModel() =
    InstrumentUiModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        description = description,
        type = type,
    )

internal fun InstrumentRoomModel.toInstrumentUiModel() =
    InstrumentUiModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        description = description,
        type = type,
    )
