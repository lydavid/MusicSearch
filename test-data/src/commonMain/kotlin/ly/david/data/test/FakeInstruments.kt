package ly.david.data.test

import ly.david.musicsearch.data.musicbrainz.Direction
import ly.david.musicsearch.data.musicbrainz.InstrumentMusicBrainzModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.data.musicbrainz.RelationMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.api.BrowseInstrumentsResponse
import ly.david.musicsearch.data.musicbrainz.api.SearchInstrumentsResponse

val fakeInstrument = InstrumentMusicBrainzModel(
    id = "instrument1",
    name = "Instrument Name",
    relations = listOf(
        RelationMusicBrainzModel(
            type = "",
            typeId = "0b67183b-9f36-4b09-b561-0fa531508f91",
            direction = Direction.BACKWARD,
            targetType = MusicBrainzEntity.AREA,
            area = ontario,
        ),
    ),
)

val fakeInstruments = listOf(
    fakeInstrument,
)

val browseInstrumentsResponse = BrowseInstrumentsResponse(
    count = 1,
    offset = 0,
    listOf(element = fakeInstrument),
)

val searchInstrumentsResponse = SearchInstrumentsResponse(
    count = 1,
    offset = 0,
    listOf(element = fakeInstrument),
)
