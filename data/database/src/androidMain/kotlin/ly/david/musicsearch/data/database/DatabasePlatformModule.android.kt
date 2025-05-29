package ly.david.musicsearch.data.database

import android.content.Context
import android.os.Build
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import io.requery.android.database.sqlite.RequerySQLiteOpenHelperFactory
import ly.david.musicsearch.shared.domain.ExportDatabase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val databasePlatformModule = module {
    single {
        DriverFactory(context = get()).createDriver()
    }

    singleOf(::ExportDatabaseImpl) bind ExportDatabase::class
}

private class DriverFactory(private val context: Context) {
    fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            schema = Database.Schema,
            context = context,
            name = DATABASE_FILE_FULL_NAME,
            factory = if (BuildConfig.DEBUG && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                // Support database inspector in Android Studio
                FrameworkSQLiteOpenHelperFactory()
            } else {
                RequerySQLiteOpenHelperFactory()
            },
            callback = object : AndroidSqliteDriver.Callback(Database.Schema) {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onConfigure(db)
                    db.setForeignKeyConstraintsEnabled(true)
                }
            },
        )
    }
}
