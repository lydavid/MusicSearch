package ly.david.data.network

val fakeTrack = TrackMusicBrainzModel(
    id = "track1",
    position = 0,
    number = "A1",
    title = "Fake Track",
    length = 25300000,
    recording = fakeRecording
)

val fakeTrack2 = TrackMusicBrainzModel(
    id = "track2",
    position = 1,
    number = "A2",
    title = "Fake Track 2 (find me)",
    length = 1000,
    recording = fakeRecording
)

val fakeMedia = MediumMusicBrainzModel(
    position = 1,
    title = null,
    trackCount = 1,
    format = "CD",
    tracks = listOf(fakeTrack, fakeTrack2)
)
