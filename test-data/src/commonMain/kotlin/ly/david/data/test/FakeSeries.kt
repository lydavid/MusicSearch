package ly.david.data.test

import ly.david.musicsearch.data.musicbrainz.api.BrowseSeriesResponse
import ly.david.musicsearch.data.musicbrainz.api.SearchSeriesResponse
import ly.david.musicsearch.data.musicbrainz.models.core.SeriesMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.relation.Direction
import ly.david.musicsearch.data.musicbrainz.models.relation.SerializableMusicBrainzEntity
import ly.david.musicsearch.data.musicbrainz.models.relation.RelationMusicBrainzModel

val fakeSeries = SeriesMusicBrainzModel(
    id = "series1",
    name = "Series Name",
    relations = listOf(
        RelationMusicBrainzModel(
            type = "",
            typeId = "281bf307-f1e8-4a56-a7b8-fe8304bb2bf3",
            direction = Direction.BACKWARD,
            targetType = SerializableMusicBrainzEntity.AREA,
            area = ontarioAreaMusicBrainzModel,
        ),
    ),
)

val fakeSeriesList = listOf(
    fakeSeries,
)

val browseSeriesResponse = BrowseSeriesResponse(
    count = 1,
    offset = 0,
    listOf(element = fakeSeries),
)

val searchSeriesResponse = SearchSeriesResponse(
    count = 1,
    offset = 0,
    listOf(element = fakeSeries),
)
