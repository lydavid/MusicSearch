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
    factory { AreaDao(get()) }
    factory { AreaPlaceDao(get(), get()) }
    factory { ArtistCreditDao(get(), get()) }
    factory { ArtistDao(get()) }
    factory { ArtistReleaseDao(get(), get()) }
    factory { ArtistReleaseGroupDao(get(), get()) }
    factory { BrowseEntityCountDao(get(), get()) }
    factory { CollectionDao(get(), get()) }
    factory { CollectionEntityDao(get(), get()) }
    factory { CountryCodeDao(get()) }
    factory { EntityHasRelationsDao(get()) }
    factory { EntityHasUrlsDao(get()) }
    factory { EventDao(get()) }
    factory { EventPlaceDao(get(), get()) }
    factory { InstrumentDao(get()) }
    factory { LabelDao(get()) }
    factory { LookupHistoryDao(get(), get()) }
    factory<ImageUrlDao> { MbidImageDao(get()) }
    factory { MediumDao(get(), get()) }
    factory { NowPlayingHistoryDao(get(), get()) }
    factory { PlaceDao(get()) }
    factory { RecordingDao(get(), get()) }
    factory { RecordingReleaseDao(get(), get()) }
    factory { RecordingWorkDao(get(), get()) }
    factory { RelationDao(get(), get()) }
    factory { ReleaseCountryDao(get(), get()) }
    factory { ReleaseDao(get(), get(), get()) }
    factory { ReleaseGroupDao(get(), get()) }
    factory { ReleaseLabelDao(get(), get()) }
    factory { ReleaseReleaseGroupDao(get(), get()) }
    factory { SearchHistoryDao(get(), get()) }
    factory { SeriesDao(get()) }
    factory { TrackDao(get(), get(), get()) }
    factory { WorkAttributeDao(get()) }
    factory { WorkDao(get()) }
}
