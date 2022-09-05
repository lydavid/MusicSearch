package ly.david.mbjc.data.domain

import ly.david.mbjc.data.Series
import ly.david.mbjc.data.network.SeriesMusicBrainzModel

internal data class SeriesUiModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
//    override val typeId: String? = null,
) : Series, UiModel()

internal fun SeriesMusicBrainzModel.toSeriesUiModel() =
    SeriesUiModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
//        typeId = typeId,
    )
