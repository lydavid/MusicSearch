package ly.david.data.network

fun MusicBrainzEntity.toFakeMusicBrainzModel(): MusicBrainzModel =
    when (this) {
        MusicBrainzEntity.AREA -> ontario
        MusicBrainzEntity.ARTIST -> davidBowie
        MusicBrainzEntity.EVENT -> fakeEvent
        MusicBrainzEntity.GENRE -> fakeGenre
        MusicBrainzEntity.INSTRUMENT -> fakeInstrument
        MusicBrainzEntity.LABEL -> elektra
        MusicBrainzEntity.PLACE -> fakePlace
        MusicBrainzEntity.RECORDING -> underPressureRecording
        MusicBrainzEntity.RELEASE -> underPressure
        MusicBrainzEntity.RELEASE_GROUP -> underPressureReleaseGroup
        MusicBrainzEntity.SERIES -> fakeSeries
        MusicBrainzEntity.WORK -> fakeWorkWithAllData
        MusicBrainzEntity.URL,
        MusicBrainzEntity.COLLECTION -> error("Not supported.")
    }
