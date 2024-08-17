package ly.david.musicsearch.shared.domain.series

import ly.david.musicsearch.shared.domain.series.SeriesDetailsModel

interface SeriesRepository {
    suspend fun lookupSeries(seriesId: String): SeriesDetailsModel
}
