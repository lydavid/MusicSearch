package ly.david.musicsearch.data.database

import ly.david.data.core.image.ImageUrlDao
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.AreaPlaceDao
import ly.david.musicsearch.data.database.dao.ArtistCreditDao
import ly.david.musicsearch.data.database.dao.ArtistDao
import ly.david.musicsearch.data.database.dao.ArtistReleaseDao
import ly.david.musicsearch.data.database.dao.ArtistReleaseGroupDao
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.CollectionDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.CountryCodeDao
import ly.david.musicsearch.data.database.dao.EntityHasRelationsDao
import ly.david.musicsearch.data.database.dao.EntityHasUrlsDao
import ly.david.musicsearch.data.database.dao.EventDao
import ly.david.musicsearch.data.database.dao.EventPlaceDao
import ly.david.musicsearch.data.database.dao.InstrumentDao
import ly.david.musicsearch.data.database.dao.LabelDao
import ly.david.musicsearch.data.database.dao.LookupHistoryDao
import ly.david.musicsearch.data.database.dao.MbidImageDao
import ly.david.musicsearch.data.database.dao.MediumDao
import ly.david.musicsearch.data.database.dao.NowPlayingHistoryDao
import ly.david.musicsearch.data.database.dao.PlaceDao
import ly.david.musicsearch.data.database.dao.RecordingDao
import ly.david.musicsearch.data.database.dao.RecordingReleaseDao
import ly.david.musicsearch.data.database.dao.RecordingWorkDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.database.dao.ReleaseCountryDao
import ly.david.musicsearch.data.database.dao.ReleaseDao
import ly.david.musicsearch.data.database.dao.ReleaseGroupDao
import ly.david.musicsearch.data.database.dao.ReleaseLabelDao
import ly.david.musicsearch.data.database.dao.ReleaseReleaseGroupDao
import ly.david.musicsearch.data.database.dao.SearchHistoryDao
import ly.david.musicsearch.data.database.dao.SeriesDao
import ly.david.musicsearch.data.database.dao.TrackDao
import ly.david.musicsearch.data.database.dao.WorkAttributeDao
import ly.david.musicsearch.data.database.dao.WorkDao
import org.koin.dsl.module

val databaseDaoModule = module {
    single { AreaDao(get()) }
    single { AreaPlaceDao(get(), get()) }
    single { ArtistCreditDao(get()) }
    single { ArtistDao(get()) }
    single { ArtistReleaseDao(get(), get()) }
    single { ArtistReleaseGroupDao(get(), get()) }
    single { BrowseEntityCountDao(get(), get()) }
    single { CollectionDao(get(), get()) }
    single { CollectionEntityDao(get(), get()) }
    single { CountryCodeDao(get()) }
    single { EntityHasRelationsDao(get()) }
    single { EntityHasUrlsDao(get()) }
    single { EventDao(get()) }
    single { EventPlaceDao(get(), get()) }
    single { InstrumentDao(get()) }
    single { LabelDao(get()) }
    single { LookupHistoryDao(get(), get()) }
    single<ImageUrlDao> { MbidImageDao(get()) }
    single { MediumDao(get(), get()) }
    single { NowPlayingHistoryDao(get(), get()) }
    single { PlaceDao(get()) }
    single { RecordingDao(get(), get()) }
    single { RecordingReleaseDao(get(), get()) }
    single { RecordingWorkDao(get(), get()) }
    single { RelationDao(get(), get()) }
    single { ReleaseCountryDao(get(), get()) }
    single { ReleaseDao(get(), get(), get()) }
    single { ReleaseGroupDao(get(), get()) }
    single { ReleaseLabelDao(get(), get()) }
    single { ReleaseReleaseGroupDao(get(), get()) }
    single { SearchHistoryDao(get(), get()) }
    single { SeriesDao(get()) }
    single { TrackDao(get(), get(), get()) }
    single { WorkAttributeDao(get()) }
    single { WorkDao(get()) }
}
