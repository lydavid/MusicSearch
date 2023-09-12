package ly.david.data.di.room

import ly.david.data.core.image.ImageUrlSaver
import ly.david.data.room.MusicSearchDatabase
import org.koin.dsl.module

val databaseDaoModule = module {
    factory {
        get<MusicSearchDatabase>().getArtistDao()
    }

    factory {
        get<MusicSearchDatabase>().getArtistReleaseDao()
    }

    factory {
        get<MusicSearchDatabase>().getArtistReleaseGroupDao()
    }

    factory {
        get<MusicSearchDatabase>().getReleaseGroupDao()
    }

    factory {
        get<MusicSearchDatabase>().getReleaseReleaseGroupDao()
    }

    factory {
        get<MusicSearchDatabase>().getReleaseDao()
    }

    factory {
        get<MusicSearchDatabase>().getMediumDao()
    }

    factory {
        get<MusicSearchDatabase>().getTrackDao()
    }

    factory {
        get<MusicSearchDatabase>().getRecordingDao()
    }

    factory {
        get<MusicSearchDatabase>().getRecordingReleaseDao()
    }

    factory {
        get<MusicSearchDatabase>().getWorkDao()
    }

    factory {
        get<MusicSearchDatabase>().getRecordingWorkDao()
    }

    factory {
        get<MusicSearchDatabase>().getAreaDao()
    }

    factory {
        get<MusicSearchDatabase>().getAreaPlaceDao()
    }

    factory {
        get<MusicSearchDatabase>().getEventPlaceDao()
    }

    factory {
        get<MusicSearchDatabase>().getReleaseCountryDao()
    }

    factory {
        get<MusicSearchDatabase>().getPlaceDao()
    }

    factory {
        get<MusicSearchDatabase>().getInstrumentDao()
    }

    factory {
        get<MusicSearchDatabase>().getLabelDao()
    }

    factory {
        get<MusicSearchDatabase>().getReleaseLabelDao()
    }

    factory {
        get<MusicSearchDatabase>().getEventDao()
    }

    factory {
        get<MusicSearchDatabase>().getSeriesDao()
    }

    factory {
        get<MusicSearchDatabase>().getRelationDao()
    }

    factory {
        get<MusicSearchDatabase>().getLookupHistoryDao()
    }

    factory {
        get<MusicSearchDatabase>().getSearchHistoryDao()
    }

    factory {
        get<MusicSearchDatabase>().getNowPlayingHistoryDao()
    }

    factory {
        get<MusicSearchDatabase>().getCollectionDao()
    }

    factory {
        get<MusicSearchDatabase>().getCollectionEntityDao()
    }

    factory<ImageUrlSaver> {
        get<MusicSearchDatabase>().getMbidImageDao()
    }
}
