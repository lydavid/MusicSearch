package ly.david.data.di.room

import ly.david.data.room.MusicSearchDatabase
import org.koin.dsl.module

val roomDatabaseDaoModule = module {
    factory {
        get<MusicSearchDatabase>().getArtistReleaseDao()
    }

    factory {
        get<MusicSearchDatabase>().getArtistReleaseGroupDao()
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
        get<MusicSearchDatabase>().getRecordingReleaseDao()
    }

    factory {
        get<MusicSearchDatabase>().getPlaceDao()
    }

    factory {
        get<MusicSearchDatabase>().getLabelDao()
    }

    factory {
        get<MusicSearchDatabase>().getReleaseLabelDao()
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
}
