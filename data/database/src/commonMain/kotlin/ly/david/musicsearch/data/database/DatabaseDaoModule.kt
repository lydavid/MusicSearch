package ly.david.musicsearch.data.database

import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.ArtistCollaborationDao
import ly.david.musicsearch.data.database.dao.ArtistCreditDao
import ly.david.musicsearch.data.database.dao.ArtistCreditDaoImpl
import ly.david.musicsearch.data.database.dao.ArtistDao
import ly.david.musicsearch.data.database.dao.ArtistReleaseDao
import ly.david.musicsearch.data.database.dao.ArtistReleaseGroupDao
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.CollectionDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.CountryCodeDao
import ly.david.musicsearch.data.database.dao.EntityHasRelationsDao
import ly.david.musicsearch.data.database.dao.EventDao
import ly.david.musicsearch.data.database.dao.GenreDao
import ly.david.musicsearch.data.database.dao.InstrumentDao
import ly.david.musicsearch.data.database.dao.LabelDao
import ly.david.musicsearch.data.database.dao.LabelsByEntityDao
import ly.david.musicsearch.data.database.dao.LookupHistoryDao
import ly.david.musicsearch.data.database.dao.MbidImageDao
import ly.david.musicsearch.data.database.dao.MbidWikipediaDaoImpl
import ly.david.musicsearch.data.database.dao.MediumDao
import ly.david.musicsearch.data.database.dao.NowPlayingHistoryDao
import ly.david.musicsearch.data.database.dao.PlaceDao
import ly.david.musicsearch.data.database.dao.RecordingDao
import ly.david.musicsearch.data.database.dao.RecordingReleaseDao
import ly.david.musicsearch.data.database.dao.RecordingsByEntityDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.database.dao.ReleaseCountryDao
import ly.david.musicsearch.data.database.dao.ReleaseDao
import ly.david.musicsearch.data.database.dao.ReleaseGroupDao
import ly.david.musicsearch.data.database.dao.ReleaseGroupDaoImpl
import ly.david.musicsearch.data.database.dao.ReleaseLabelDao
import ly.david.musicsearch.data.database.dao.ReleaseReleaseGroupDao
import ly.david.musicsearch.data.database.dao.SearchHistoryDao
import ly.david.musicsearch.data.database.dao.SearchResultDao
import ly.david.musicsearch.data.database.dao.SeriesDao
import ly.david.musicsearch.data.database.dao.SpotifyHistoryDao
import ly.david.musicsearch.data.database.dao.TrackDao
import ly.david.musicsearch.data.database.dao.VisitedDaoImpl
import ly.david.musicsearch.data.database.dao.WorkAttributeDao
import ly.david.musicsearch.data.database.dao.WorkDao
import ly.david.musicsearch.data.database.dao.WorksByEntityDao
import ly.david.musicsearch.shared.domain.history.VisitedDao
import ly.david.musicsearch.shared.domain.image.ImageUrlDao
import ly.david.musicsearch.shared.domain.wikimedia.MbidWikipediaDao
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val databaseDaoModule = module {
    singleOf(::AreaDao)
    singleOf(::ArtistCreditDaoImpl) bind ArtistCreditDao::class
    singleOf(::ArtistDao)
    single { ArtistCollaborationDao(get()) }
    single { ArtistReleaseDao(get(), get()) }
    single { ArtistReleaseGroupDao(get(), get()) }
    single { BrowseEntityCountDao(get(), get()) }
    single { CollectionDao(get(), get()) }
    single { CollectionEntityDao(get(), get()) }
    single { CountryCodeDao(get()) }
    single { EntityHasRelationsDao(get()) }
    singleOf(::VisitedDaoImpl) bind VisitedDao::class
    singleOf(::EventDao)
    single { InstrumentDao(get()) }
    single { GenreDao(get()) }
    single { LabelDao(get()) }
    single { LabelsByEntityDao(get(), get()) }
    single { LookupHistoryDao(get(), get()) }
    singleOf(::MbidImageDao) bind ImageUrlDao::class
    single { MediumDao(get(), get()) }
    single { NowPlayingHistoryDao(get(), get()) }
    singleOf(::PlaceDao)
    single { RecordingDao(get(), get()) }
    single { RecordingReleaseDao(get(), get()) }
    single { RecordingsByEntityDao(get(), get()) }
    single { RelationDao(get(), get()) }
    single { ReleaseCountryDao(get(), get()) }
    single { ReleaseDao(get(), get(), get()) }
    singleOf(::ReleaseGroupDaoImpl) bind ReleaseGroupDao::class
    single { ReleaseLabelDao(get(), get()) }
    single { ReleaseReleaseGroupDao(get(), get()) }
    single { SearchHistoryDao(get(), get()) }
    single { SeriesDao(get()) }
    single { TrackDao(get(), get(), get()) }
    single { WorkAttributeDao(get()) }
    single { WorkDao(get()) }
    single { WorksByEntityDao(get(), get()) }
    single { SpotifyHistoryDao(get(), get()) }
    singleOf(::MbidWikipediaDaoImpl) bind MbidWikipediaDao::class
    single { SearchResultDao(get(), get()) }
}
