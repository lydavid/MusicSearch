package ly.david.data.domain.listitem

import ly.david.data.musicbrainz.SeriesMusicBrainzModel
import lydavidmusicsearchdatadatabase.Series

data class SeriesListItemModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
) : ly.david.data.core.Series, ListItemModel()

fun SeriesMusicBrainzModel.toSeriesListItemModel() =
    SeriesListItemModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
    )

fun Series.toSeriesListItemModel() =
    SeriesListItemModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
    )
