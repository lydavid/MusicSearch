package ly.david.data.network

import ly.david.data.network.api.SearchInstrumentsResponse

val fakeInstrument = InstrumentMusicBrainzModel(
    id = "instrument1",
    name = "Instrument Name",
)

val fakeInstruments = listOf(
    fakeInstrument
)

val searchInstrumentsResponse = SearchInstrumentsResponse(
    count = 1,
    offset = 0,
    listOf(element = fakeInstrument)
)
