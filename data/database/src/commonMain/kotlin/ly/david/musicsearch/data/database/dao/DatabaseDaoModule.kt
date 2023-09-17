package ly.david.musicsearch.data.database.dao

import org.koin.dsl.module

val databaseDaoModule = module {
    single { AreaDao(get()) }
    single { AreaPlaceDao(get()) }
    single { EntityHasRelationsDao(get()) }
    single { EntityHasUrlsDao(get()) }
    single { EventDao(get()) }
    single { PlaceDao(get()) }
    single { RelationDao(get()) }
}
