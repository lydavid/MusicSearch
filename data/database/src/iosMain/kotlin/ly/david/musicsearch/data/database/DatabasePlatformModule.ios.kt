package ly.david.musicsearch.data.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import ly.david.musicsearch.shared.domain.ExportDatabase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val databasePlatformModule = module {
    single {
        DriverFactory().createDriver()
    }

    singleOf(::ExportDatabaseImpl) bind ExportDatabase::class
}

private class DriverFactory {
    fun createDriver(): SqlDriver {
        val driver = NativeSqliteDriver(Database.Schema, "music_search.db")
//        Database.Schema.create(driver)
        return driver
    }
}
