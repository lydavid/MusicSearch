package ly.david.data.test

import ly.david.data.musicbrainz.Direction
import ly.david.musicsearch.data.core.network.MusicBrainzEntity
import ly.david.data.musicbrainz.RelationMusicBrainzModel
import ly.david.data.musicbrainz.SeriesMusicBrainzModel
import ly.david.data.musicbrainz.api.BrowseSeriesResponse
import ly.david.data.musicbrainz.api.SearchSeriesResponse

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
