package ly.david.musicsearch.data.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import org.koin.dsl.module

actual val databaseDriverModule = module {
    single {
        DriverFactory().createDriver()
    }
}

private class DriverFactory {
    fun createDriver(): SqlDriver {
        val driver = NativeSqliteDriver(Database.Schema, "music_search.db")
//        Database.Schema.create(driver)
        return driver
    }
}
