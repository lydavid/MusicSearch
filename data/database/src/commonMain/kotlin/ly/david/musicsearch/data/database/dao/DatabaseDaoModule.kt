package ly.david.musicsearch.data.database.dao

import org.koin.dsl.module

val databaseDaoModule = module {
    single { EntityHasRelationsDao(get()) }
    single { EntityHasUrlsDao(get()) }
    single { EventDao(get()) }
    single { RelationDao(get()) }
}
