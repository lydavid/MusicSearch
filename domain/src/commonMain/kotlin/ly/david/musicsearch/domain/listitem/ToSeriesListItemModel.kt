package ly.david.musicsearch.domain.listitem

import ly.david.data.musicbrainz.SeriesMusicBrainzModel
import ly.david.musicsearch.data.core.listitem.SeriesListItemModel
import lydavidmusicsearchdatadatabase.Series

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
