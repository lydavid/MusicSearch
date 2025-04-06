package ly.david.musicsearch.data.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import ly.david.musicsearch.shared.domain.ExportDatabase
import ly.david.musicsearch.shared.domain.PACKAGE_NAME
import me.sujanpoudel.utils.paths.appCacheDirectory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import java.util.Properties

actual val databasePlatformModule = module {
    single {
        DriverFactory().createDriver()
    }

    singleOf(::ExportDatabaseImpl) bind ExportDatabase::class
}

private class DriverFactory {
    fun createDriver(): SqlDriver {
        val directory = appCacheDirectory(PACKAGE_NAME)
        val driver = JdbcSqliteDriver(
            url = "jdbc:sqlite:$directory/$DATABASE_FILE_FULL_NAME",
            schema = Database.Schema,
            properties = Properties().apply { put("foreign_keys", "true") },
        )
        return driver
    }
}
