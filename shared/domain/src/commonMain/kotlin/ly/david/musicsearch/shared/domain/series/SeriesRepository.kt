package ly.david.musicsearch.shared.domain.series

interface SeriesRepository {
    suspend fun lookupSeries(
        seriesId: String,
        forceRefresh: Boolean,
    ): SeriesDetailsModel
}
