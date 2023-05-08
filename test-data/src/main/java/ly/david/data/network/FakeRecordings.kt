package ly.david.data.network

import ly.david.data.network.api.BrowseRecordingsResponse
import ly.david.data.network.api.SearchRecordingsResponse

val fakeRecording = RecordingMusicBrainzModel(
    id = "recording1",
    name = "Fake Recording",
    firstReleaseDate = "2022-11-06",
    length = 25300000,
    artistCredits = listOf(davidBowieArtistCredit, queenArtistCredit),
    relations = listOf(
        RelationMusicBrainzModel(
            type = "",
            typeId = "4f4aa317-c3c4-4001-ac23-fb8cf0bc543c",
            direction = Direction.BACKWARD,
            targetType = MusicBrainzResource.AREA,
            area = ontario
        )
    )
)

val fakeRecordings = listOf(
    fakeRecording
)

val browseRecordingsResponse = BrowseRecordingsResponse(
    count = 1,
    offset = 0,
    musicBrainzModels = listOf(fakeRecording)
)

val searchRecordingsResponse = SearchRecordingsResponse(
    count = 1,
    offset = 0,
    listOf(element = fakeRecording)
)
