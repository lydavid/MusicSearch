package ly.david.data.network

import ly.david.data.network.api.BrowseEventsResponse
import ly.david.data.network.api.SearchEventsResponse

val fakeEvent = EventMusicBrainzModel(
    id = "event1",
    name = "Event Name"
)

val fakeEvents = listOf(
    fakeEvent
)

val browseEventsResponse = BrowseEventsResponse(
    count = 1,
    offset = 0,
    events = listOf(fakeEvent)
)

val searchEventsResponse = SearchEventsResponse(
    count = 1,
    offset = 0,
    listOf(element = fakeEvent)
)
