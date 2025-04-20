package ly.david.musicsearch.data.database

import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.ArtistCollaborationDao
import ly.david.musicsearch.data.database.dao.ArtistCreditDao
import ly.david.musicsearch.data.database.dao.ArtistCreditDaoImpl
import ly.david.musicsearch.data.database.dao.ArtistDao
import ly.david.musicsearch.data.database.dao.BrowseRemoteCountDao
import ly.david.musicsearch.data.database.dao.CollectionDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.CountryCodeDao
import ly.david.musicsearch.data.database.dao.DatabaseMetadataDao
import ly.david.musicsearch.data.database.dao.EntityHasRelationsDao
import ly.david.musicsearch.data.database.dao.EventDao
import ly.david.musicsearch.data.database.dao.GenreDao
import ly.david.musicsearch.data.database.dao.InstrumentDao
import ly.david.musicsearch.data.database.dao.LabelDao
import ly.david.musicsearch.data.database.dao.LookupHistoryDao
import ly.david.musicsearch.data.database.dao.MbidImageDao
import ly.david.musicsearch.data.database.dao.MbidWikipediaDaoImpl
import ly.david.musicsearch.data.database.dao.MediumDao
import ly.david.musicsearch.data.database.dao.NowPlayingHistoryDao
import ly.david.musicsearch.data.database.dao.PlaceDao
import ly.david.musicsearch.data.database.dao.RecordingDao
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
    single { BrowseRemoteCountDao(get(), get()) }
    single { CollectionDao(get(), get()) }
    singleOf(::CollectionEntityDao)
    single { CountryCodeDao(get()) }
    single { EntityHasRelationsDao(get()) }
    singleOf(::VisitedDaoImpl) bind VisitedDao::class
    singleOf(::EventDao)
    singleOf(::InstrumentDao)
    singleOf(::GenreDao)
    singleOf(::LabelDao)
    single { LookupHistoryDao(get(), get()) }
    singleOf(::MbidImageDao) bind ImageUrlDao::class
    single { MediumDao(get(), get()) }
    single { NowPlayingHistoryDao(get(), get()) }
    singleOf(::PlaceDao)
    singleOf(::RecordingDao)
    single { RelationDao(get(), get()) }
    singleOf(::ReleaseCountryDao)
    singleOf(::ReleaseDao)
    singleOf(::ReleaseGroupDaoImpl) bind ReleaseGroupDao::class
    single { ReleaseLabelDao(get()) }
    single { ReleaseReleaseGroupDao(get()) }
    single { SearchHistoryDao(get(), get()) }
    singleOf(::SeriesDao)
    single { TrackDao(get(), get(), get()) }
    single { WorkAttributeDao(get()) }
    singleOf(::WorkDao)
    single { SpotifyHistoryDao(get(), get()) }
    singleOf(::MbidWikipediaDaoImpl) bind MbidWikipediaDao::class
    single { SearchResultDao(get(), get()) }
    singleOf(::DatabaseMetadataDao)
}
