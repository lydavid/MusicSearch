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
        get<MusicSearchDatabase>().getRecordingReleaseDao()
    }

    factory {
        get<MusicSearchDatabase>().getReleaseLabelDao()
    }

    factory {
        get<MusicSearchDatabase>().getRelationDao()
    }

    factory {
        get<MusicSearchDatabase>().getSearchHistoryDao()
    }

    factory {
        get<MusicSearchDatabase>().getNowPlayingHistoryDao()
    }

    factory {
        get<MusicSearchDatabase>().getCollectionEntityDao()
    }
}
