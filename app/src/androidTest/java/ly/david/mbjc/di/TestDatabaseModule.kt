package ly.david.mbjc.di

import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.createDatabase
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

// TODO: dupe
//val testRoomDatabaseModule = module {
//    single {
//        Room.inMemoryDatabaseBuilder(
//            get(),
//            MusicSearchRoomDatabase::class.java
//        )
//            .allowMainThreadQueries()
//            .build()
//    } binds arrayOf(MusicSearchDatabase::class, MusicSearchRoomDatabase::class)
//}

//val testDatabaseModule = module {
//    single<QueryResult.Value<Unit>> {
//        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
////        createDatabase(driver)
//        Database.Schema.create(driver)
//    }
//}

val testDatabaseModule: Module = module {
    scope(named("test")) {
        scoped {
            createDatabase(driver = get())
        }
    }
}

val testDatabaseDriverModule = module {
    factory<SqlDriver> {
        AndroidSqliteDriver(
            schema = Database.Schema,
            context = get(),
            name = null,
            callback = object : AndroidSqliteDriver.Callback(Database.Schema) {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onConfigure(db)
                    db.setForeignKeyConstraintsEnabled(true)
                }
            }
        )
    }
}
