package ly.david.data.test

import ly.david.musicsearch.data.musicbrainz.api.BrowseRecordingsResponse
import ly.david.musicsearch.data.musicbrainz.api.SearchRecordingsResponse
import ly.david.musicsearch.data.musicbrainz.models.core.RecordingMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.relation.Direction
import ly.david.musicsearch.data.musicbrainz.models.relation.SerializableMusicBrainzEntity
import ly.david.musicsearch.data.musicbrainz.models.relation.RelationMusicBrainzModel

val underPressureRecording = RecordingMusicBrainzModel(
    id = "32c7e292-14f1-4080-bddf-ef852e0a4c59",
    name = "Under Pressure",
    firstReleaseDate = "1981-10",
    length = 241000,
    artistCredits = listOf(
        queenArtistCredit,
        davidBowieArtistCredit,
    ),
    relations = listOf(
        RelationMusicBrainzModel(
            type = "producer",
            typeId = "5c0ceac3-feb4-41f0-868d-dc06f6e27fc0",
            direction = Direction.BACKWARD,
            targetType = SerializableMusicBrainzEntity.ARTIST,
            artist = davidBowie,
        ),
    ),
)

val fakeRecordings = listOf(
    underPressureRecording,
)

val browseRecordingsResponse = BrowseRecordingsResponse(
    count = 1,
    offset = 0,
    musicBrainzModels = listOf(underPressureRecording),
)

val searchRecordingsResponse = SearchRecordingsResponse(
    count = 1,
    offset = 0,
    listOf(element = underPressureRecording),
)
