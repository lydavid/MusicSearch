package ly.david.musicsearch.domain.series

import ly.david.musicsearch.core.models.series.SeriesScaffoldModel

interface SeriesRepository {
    suspend fun lookupSeries(seriesId: String): SeriesScaffoldModel
}
