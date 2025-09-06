package ly.david.musicsearch.data.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import ly.david.musicsearch.shared.domain.APPLICATION_ID
import ly.david.musicsearch.shared.domain.ExportDatabase
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

internal val databasePath = "${appCacheDirectory(APPLICATION_ID)}/$DATABASE_FILE_FULL_NAME"

private class DriverFactory {
    fun createDriver(): SqlDriver {
        val driver = JdbcSqliteDriver(
            url = "jdbc:sqlite:$databasePath",
            schema = Database.Schema,
            // https://github.com/sqldelight/sqldelight/issues/4931
            properties = Properties().apply { put("foreign_keys", "false") },
        )
        return driver
    }
}
