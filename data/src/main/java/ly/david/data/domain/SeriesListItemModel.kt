package ly.david.data.domain

import ly.david.data.Series
import ly.david.data.network.SeriesMusicBrainzModel
import ly.david.data.persistence.series.SeriesRoomModel

data class SeriesListItemModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
) : Series, ListItemModel()

fun SeriesMusicBrainzModel.toSeriesListItemModel() =
    SeriesListItemModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
    )

fun SeriesRoomModel.toSeriesListItemModel() =
    SeriesListItemModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
    )
