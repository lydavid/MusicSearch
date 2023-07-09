package ly.david.data.network

import ly.david.data.network.api.BrowseSeriesResponse
import ly.david.data.network.api.SearchSeriesResponse

val fakeSeries = SeriesMusicBrainzModel(
    id = "series1",
    name = "Series Name",
    relations = listOf(
        RelationMusicBrainzModel(
            type = "",
            typeId = "281bf307-f1e8-4a56-a7b8-fe8304bb2bf3",
            direction = Direction.BACKWARD,
            targetType = MusicBrainzEntity.AREA,
            area = ontario
        )
    )
)

val fakeSeriesList = listOf(
    fakeSeries
)

val browseSeriesResponse = BrowseSeriesResponse(
    count = 1,
    offset = 0,
    listOf(element = fakeSeries)
)

val searchSeriesResponse = SearchSeriesResponse(
    count = 1,
    offset = 0,
    listOf(element = fakeSeries)
)
