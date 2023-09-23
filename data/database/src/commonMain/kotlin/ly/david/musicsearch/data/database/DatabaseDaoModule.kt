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
import ly.david.musicsearch.data.database.dao.MbidImageDao
import ly.david.musicsearch.data.database.dao.MediumDao
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
import ly.david.musicsearch.data.database.dao.SeriesDao
import ly.david.musicsearch.data.database.dao.TrackDao
import ly.david.musicsearch.data.database.dao.WorkAttributeDao
import ly.david.musicsearch.data.database.dao.WorkDao
import org.koin.dsl.module

val databaseDaoModule = module {
    factory { AreaDao(get()) }
    factory { AreaPlaceDao(get()) }
    factory { ArtistCreditDao(get(), get()) }
    factory { ArtistDao(get()) }
    factory { ArtistReleaseDao(get()) }
    factory { ArtistReleaseGroupDao(get()) }
    factory { BrowseEntityCountDao(get()) }
    factory { CollectionDao(get()) }
    factory { CollectionEntityDao(get()) }
    factory { CountryCodeDao(get()) }
    factory { EntityHasRelationsDao(get()) }
    factory { EntityHasUrlsDao(get()) }
    factory { EventDao(get()) }
    factory { EventPlaceDao(get()) }
    factory { InstrumentDao(get()) }
    factory { LabelDao(get()) }
    factory<ImageUrlDao> { MbidImageDao(get()) }
    factory { MediumDao(get(), get()) }
    factory { PlaceDao(get()) }
    factory { RecordingDao(get(), get()) }
    factory { RecordingReleaseDao(get()) }
    factory { RecordingWorkDao(get()) }
    factory { RelationDao(get()) }
    factory { ReleaseCountryDao(get()) }
    factory { ReleaseDao(get(), get(), get()) }
    factory { ReleaseGroupDao(get(), get()) }
    factory { ReleaseLabelDao(get()) }
    factory { ReleaseReleaseGroupDao(get()) }
    factory { SeriesDao(get()) }
    factory { TrackDao(get(), get()) }
    factory { WorkAttributeDao(get()) }
    factory { WorkDao(get()) }
}
