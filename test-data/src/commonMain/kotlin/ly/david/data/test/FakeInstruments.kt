package ly.david.data.test

import ly.david.musicsearch.data.musicbrainz.api.BrowseInstrumentsResponse
import ly.david.musicsearch.data.musicbrainz.api.SearchInstrumentsResponse
import ly.david.musicsearch.data.musicbrainz.models.core.InstrumentMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.relation.Direction
import ly.david.musicsearch.data.musicbrainz.models.relation.SerializableMusicBrainzEntity
import ly.david.musicsearch.data.musicbrainz.models.relation.RelationMusicBrainzModel

val fakeInstrument = InstrumentMusicBrainzModel(
    id = "instrument1",
    name = "Instrument Name",
    relations = listOf(
        RelationMusicBrainzModel(
            type = "",
            typeId = "0b67183b-9f36-4b09-b561-0fa531508f91",
            direction = Direction.BACKWARD,
            targetType = SerializableMusicBrainzEntity.AREA,
            area = ontarioAreaMusicBrainzModel,
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
