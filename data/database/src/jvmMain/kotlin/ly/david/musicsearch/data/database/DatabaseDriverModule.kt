package ly.david.musicsearch.data.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import ly.david.musicsearch.shared.domain.PACKAGE_NAME
import me.sujanpoudel.utils.paths.appCacheDirectory
import java.util.Properties
import org.koin.dsl.module

actual val databaseDriverModule = module {
    single {
        DriverFactory().createDriver()
    }
}

private class DriverFactory {
    fun createDriver(): SqlDriver {
        val directory = appCacheDirectory(PACKAGE_NAME)
        val driver = JdbcSqliteDriver(
            url = "jdbc:sqlite:$directory/music_search.db",
            properties = Properties().apply { put("foreign_keys", "true") }
        )
        Database.Schema.create(driver)
        return driver
    }
}
