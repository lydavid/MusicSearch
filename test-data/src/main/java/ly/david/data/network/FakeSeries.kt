package ly.david.data.network

import ly.david.data.network.api.SearchSeriesResponse

val fakeSeries = SeriesMusicBrainzModel(
    id = "series1",
    name = "Series Name",
)

val fakeSeriesList = listOf(
    fakeSeries
)

val searchSeriesResponse = SearchSeriesResponse(
    count = 1,
    offset = 0,
    listOf(element = fakeSeries)
)
