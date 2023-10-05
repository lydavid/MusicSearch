package ly.david.data.test

import ly.david.data.musicbrainz.Direction
import ly.david.data.musicbrainz.EventMusicBrainzModel
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.musicbrainz.RelationMusicBrainzModel
import ly.david.data.musicbrainz.api.BrowseEventsResponse
import ly.david.data.musicbrainz.api.SearchEventsResponse

val fakeEvent = EventMusicBrainzModel(
    id = "event1",
    name = "Event Name",
    relations = listOf(
        RelationMusicBrainzModel(
            type = "",
            typeId = "542f8484-8bc7-3ce5-a022-747850b2b928",
            direction = Direction.FORWARD,
            targetType = MusicBrainzEntity.AREA,
            area = ontario
        )
    )
)

val fakeEvents = listOf(
    fakeEvent
)

val browseEventsResponse = BrowseEventsResponse(
    count = 1,
    offset = 0,
    musicBrainzModels = listOf(fakeEvent)
)

val searchEventsResponse = SearchEventsResponse(
    count = 1,
    offset = 0,
    listOf(element = fakeEvent)
)
