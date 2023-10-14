package ly.david.musicsearch.domain.series

import ly.david.musicsearch.data.core.series.SeriesScaffoldModel

interface SeriesRepository {
    suspend fun lookupSeries(seriesId: String): SeriesScaffoldModel
}
