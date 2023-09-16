package ly.david.musicsearch.data.database.dao

import ly.david.musicsearch.data.database.dao.event.EventDao
import org.koin.dsl.module

//@Module
//@ComponentScan
//class DatabaseDaoModule

val databaseDaoModule = module {
// TODO: confirm whether singleton is correct
    single { EventDao(get()) }
}
