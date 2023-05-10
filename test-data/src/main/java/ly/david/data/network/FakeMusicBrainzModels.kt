package ly.david.data.network

fun MusicBrainzResource.toFakeMusicBrainzModel(): MusicBrainzModel =
    when (this) {
        MusicBrainzResource.AREA -> ontario
        MusicBrainzResource.ARTIST -> davidBowie
        MusicBrainzResource.EVENT -> fakeEvent
        MusicBrainzResource.GENRE -> fakeGenre
        MusicBrainzResource.INSTRUMENT -> fakeInstrument
        MusicBrainzResource.LABEL -> elektra
        MusicBrainzResource.PLACE -> fakePlace
        MusicBrainzResource.RECORDING -> underPressureRecording
        MusicBrainzResource.RELEASE -> underPressure
        MusicBrainzResource.RELEASE_GROUP -> underPressureReleaseGroup
        MusicBrainzResource.SERIES -> fakeSeries
        MusicBrainzResource.WORK -> fakeWorkWithAllData
        MusicBrainzResource.URL,
        MusicBrainzResource.COLLECTION -> error("Not supported.")
    }
