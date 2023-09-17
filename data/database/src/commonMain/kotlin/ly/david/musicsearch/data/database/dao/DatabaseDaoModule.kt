package ly.david.musicsearch.data.database.dao

import org.koin.dsl.module

val databaseDaoModule = module {
    single { AreaDao(get()) }
    single { ArtistDao(get()) }
    single { AreaPlaceDao(get()) }
    single { BrowseEntityCountDao(get()) }
    single { CollectionDao(get()) }
    single { CollectionEntityDao(get()) }
    single { EntityHasRelationsDao(get()) }
    single { EntityHasUrlsDao(get()) }
    single { EventDao(get()) }
    single { EventPlaceDao(get()) }
    single { InstrumentDao(get()) }
    single { PlaceDao(get()) }
    single { RelationDao(get()) }
}
