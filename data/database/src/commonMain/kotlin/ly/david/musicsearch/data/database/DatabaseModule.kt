package ly.david.musicsearch.data.database

import org.koin.core.module.Module
import org.koin.dsl.module

val databaseModule: Module = module {
    single {
        createDatabase(driver = get())
    }
}
