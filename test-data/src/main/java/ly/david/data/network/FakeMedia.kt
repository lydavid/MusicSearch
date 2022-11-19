package ly.david.data.network

val fakeRecording = RecordingMusicBrainzModel(
    id = "recording1",
    name = "Fake Recording",
    date = "2022-11-06",
    length = 25300000,
    artistCredits = listOf(fakeArtistCredit, fakeArtistCredit2),
)

val fakeTrack = TrackMusicBrainzModel(
    id = "track1",
    position = 0,
    number = "A1",
    title = "Fake Track",
    length = 25300000,
    recording = fakeRecording
)

val fakeMedia = MediumMusicBrainzModel(
    position = 1,
    title = null,
    trackCount = 1,
    format = "CD",
    tracks = listOf(fakeTrack)
)
