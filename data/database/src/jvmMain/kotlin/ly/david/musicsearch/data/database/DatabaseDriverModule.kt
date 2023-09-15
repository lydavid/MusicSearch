package ly.david.musicsearch.data.database

import org.koin.core.module.Module
import org.koin.dsl.module

actual val databaseDriverModule: Module = module {
    single {
        DriverFactory().createDriver()
    }
}
