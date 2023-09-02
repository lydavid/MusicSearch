package ly.david.data.test

import ly.david.data.network.Direction
import ly.david.data.network.InstrumentMusicBrainzModel
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.network.RelationMusicBrainzModel
import ly.david.data.network.api.BrowseInstrumentsResponse
import ly.david.data.network.api.SearchInstrumentsResponse

val fakeInstrument = InstrumentMusicBrainzModel(
    id = "instrument1",
    name = "Instrument Name",
    relations = listOf(
        RelationMusicBrainzModel(
            type = "",
            typeId = "0b67183b-9f36-4b09-b561-0fa531508f91",
            direction = Direction.BACKWARD,
            targetType = MusicBrainzEntity.AREA,
            area = ontario
        )
    )
)

val fakeInstruments = listOf(
    fakeInstrument
)

val browseInstrumentsResponse = BrowseInstrumentsResponse(
    count = 1,
    offset = 0,
    listOf(element = fakeInstrument)
)

val searchInstrumentsResponse = SearchInstrumentsResponse(
    count = 1,
    offset = 0,
    listOf(element = fakeInstrument)
)
