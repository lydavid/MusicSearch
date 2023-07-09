package ly.david.data.network

import ly.david.data.network.api.BrowseEventsResponse
import ly.david.data.network.api.SearchEventsResponse

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
