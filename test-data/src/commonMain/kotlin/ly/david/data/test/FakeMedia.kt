package ly.david.data.test

import ly.david.musicsearch.data.musicbrainz.models.MediumMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.TrackMusicBrainzModel

val underPressureTrack = TrackMusicBrainzModel(
    id = "ae3a221e-b7db-3234-b9c7-a3de487389ef",
    position = 1,
    number = "1",
    title = "Under Pressure",
    recording = underPressureRecordingMusicBrainzModel,
)

val soulBrotherTrack = TrackMusicBrainzModel(
    id = "track2",
    position = 2,
    number = "2",
    title = "Soul Brother",
    recording = underPressureRecordingMusicBrainzModel,
)

val underPressureMedia = MediumMusicBrainzModel(
    position = 1,
    title = "",
    trackCount = 2,
    format = "Vinyl",
    tracks = listOf(underPressureTrack, soulBrotherTrack),
)
