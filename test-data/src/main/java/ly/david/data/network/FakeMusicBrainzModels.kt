package ly.david.data.network

fun MusicBrainzResource.toFakeMusicBrainzModel(): MusicBrainzModel =
    when (this) {
        MusicBrainzResource.AREA -> ontario
        MusicBrainzResource.ARTIST -> fakeArtist
        MusicBrainzResource.EVENT -> fakeEvent
        MusicBrainzResource.GENRE -> fakeGenre
        MusicBrainzResource.INSTRUMENT -> fakeInstrument
        MusicBrainzResource.LABEL -> fakeLabel
        MusicBrainzResource.PLACE -> fakePlace
        MusicBrainzResource.RECORDING -> fakeRecording
        MusicBrainzResource.RELEASE -> fakeRelease
        MusicBrainzResource.RELEASE_GROUP -> fakeReleaseGroup
        MusicBrainzResource.SERIES -> fakeSeries
        MusicBrainzResource.WORK -> fakeWorkWithAllData
        MusicBrainzResource.URL,
        MusicBrainzResource.COLLECTION -> error("Not supported.")
    }
