package ly.david.data.network

import ly.david.data.network.api.BrowseRecordingsResponse
import ly.david.data.network.api.SearchRecordingsResponse

val fakeRecording = RecordingMusicBrainzModel(
    id = "recording1",
    name = "Fake Recording",
    firstReleaseDate = "2022-11-06",
    length = 25300000,
    artistCredits = listOf(fakeArtistCredit, fakeArtistCredit2),
)

val fakeRecordings = listOf(
    fakeRecording
)

val browseRecordingsResponse = BrowseRecordingsResponse(
    count = 1,
    offset = 0,
    recordings = listOf(fakeRecording)
)

val searchRecordingsResponse = SearchRecordingsResponse(
    count = 1,
    offset = 0,
    listOf(element = fakeRecording)
)
