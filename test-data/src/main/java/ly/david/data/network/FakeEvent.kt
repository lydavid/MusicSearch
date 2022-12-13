package ly.david.data.network

import ly.david.data.network.api.BrowseEventsResponse

val fakeEvent = EventMusicBrainzModel(
    id = "event1",
    name = "Event Name"
)

val browseEventsResponse = BrowseEventsResponse(
    count = 1,
    offset = 0,
    events = listOf(fakeEvent)
)
