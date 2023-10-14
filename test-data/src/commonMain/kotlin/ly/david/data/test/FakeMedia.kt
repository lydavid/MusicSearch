package ly.david.data.test

import ly.david.data.musicbrainz.MediumMusicBrainzModel
import ly.david.data.musicbrainz.TrackMusicBrainzModel

val underPressureTrack = TrackMusicBrainzModel(
    id = "ae3a221e-b7db-3234-b9c7-a3de487389ef",
    position = 1,
    number = "1",
    title = "Under Pressure",
    recording = underPressureRecording,
)

val soulBrotherTrack = TrackMusicBrainzModel(
    id = "track2",
    position = 2,
    number = "2",
    title = "Soul Brother",
    recording = underPressureRecording,
)

val underPressureMedia = MediumMusicBrainzModel(
    position = 1,
    title = "",
    trackCount = 2,
    format = "Vinyl",
    tracks = listOf(underPressureTrack, soulBrotherTrack),
)
