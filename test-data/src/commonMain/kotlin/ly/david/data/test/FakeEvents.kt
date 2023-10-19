package ly.david.data.test

import ly.david.musicsearch.data.musicbrainz.models.relation.Direction
import ly.david.musicsearch.data.musicbrainz.models.core.EventMusicBrainzModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.data.musicbrainz.models.relation.RelationMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.api.BrowseEventsResponse
import ly.david.musicsearch.data.musicbrainz.api.SearchEventsResponse

val fakeEvent = EventMusicBrainzModel(
    id = "event1",
    name = "Event Name",
    relations = listOf(
        RelationMusicBrainzModel(
            type = "",
            typeId = "542f8484-8bc7-3ce5-a022-747850b2b928",
            direction = Direction.FORWARD,
            targetType = MusicBrainzEntity.AREA,
            area = ontario,
        ),
    ),
)

val fakeEvents = listOf(
    fakeEvent,
)

val browseEventsResponse = BrowseEventsResponse(
    count = 1,
    offset = 0,
    musicBrainzModels = listOf(fakeEvent),
)

val searchEventsResponse = SearchEventsResponse(
    count = 1,
    offset = 0,
    listOf(element = fakeEvent),
)
