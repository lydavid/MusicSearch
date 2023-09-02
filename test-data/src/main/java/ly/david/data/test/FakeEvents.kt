package ly.david.data.test

import ly.david.data.network.Direction
import ly.david.data.network.EventMusicBrainzModel
import ly.david.data.network.MusicBrainzEntity
import ly.david.data.network.RelationMusicBrainzModel
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
