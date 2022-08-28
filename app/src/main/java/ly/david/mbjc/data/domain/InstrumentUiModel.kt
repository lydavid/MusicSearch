package ly.david.mbjc.data.domain

import ly.david.mbjc.data.Instrument
import ly.david.mbjc.data.network.InstrumentMusicBrainzModel

internal data class InstrumentUiModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val description: String? = null,
    override val type: String? = null,
) : Instrument, UiModel()

internal fun InstrumentMusicBrainzModel.toGenreUiModel() =
    InstrumentUiModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        description = description,
        type = type,
    )
