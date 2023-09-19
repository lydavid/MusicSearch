package ly.david.musicsearch.data.database

import ly.david.data.core.image.ImageUrlDao
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.AreaPlaceDao
import ly.david.musicsearch.data.database.dao.ArtistCreditDao
import ly.david.musicsearch.data.database.dao.ArtistDao
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
import ly.david.musicsearch.data.database.dao.PlaceDao
import ly.david.musicsearch.data.database.dao.RecordingDao
import ly.david.musicsearch.data.database.dao.RecordingWorkDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.database.dao.SeriesDao
import ly.david.musicsearch.data.database.dao.WorkAttributeDao
import ly.david.musicsearch.data.database.dao.WorkDao
import org.koin.dsl.module

val databaseDaoModule = module {
    single { AreaDao(get()) }
    single { AreaPlaceDao(get()) }
    single { ArtistCreditDao(get(), get()) }
    single { ArtistDao(get()) }
    single { BrowseEntityCountDao(get()) }
    single { CollectionDao(get()) }
    single { CollectionEntityDao(get()) }
    single { CountryCodeDao(get()) }
    single { EntityHasRelationsDao(get()) }
    single { EntityHasUrlsDao(get()) }
    single { EventDao(get()) }
    single { EventPlaceDao(get()) }
    single { InstrumentDao(get()) }
    single { LabelDao(get()) }
    single<ImageUrlDao> { MbidImageDao(get()) }
    single { PlaceDao(get()) }
    single { RecordingDao(get()) }
    single { RecordingWorkDao(get()) }
    single { RelationDao(get()) }
    single { SeriesDao(get()) }
    single { WorkAttributeDao(get()) }
    single { WorkDao(get()) }
}
