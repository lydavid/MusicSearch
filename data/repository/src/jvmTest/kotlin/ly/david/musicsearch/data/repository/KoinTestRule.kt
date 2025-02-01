package ly.david.musicsearch.data.repository

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import ly.david.musicsearch.core.coroutines.CoroutineDispatchers
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.createDatabase
import ly.david.musicsearch.data.database.databaseDaoModule
import ly.david.musicsearch.data.repository.di.repositoryDataModule
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import java.util.Properties

@OptIn(ExperimentalCoroutinesApi::class)
val testCoroutineDispatchersModule = module {
    factory {
        val testDispatcher = UnconfinedTestDispatcher()
        CoroutineDispatchers(
            default = testDispatcher,
            io = testDispatcher,
        )
    }
}

val testDatabaseModule = module {
    single<Database> {
        val driver = JdbcSqliteDriver(
            url = JdbcSqliteDriver.IN_MEMORY,
            properties = Properties().apply {
                put(
                    "foreign_keys",
                    "true",
                )
            },
        )
        Database.Schema.create(driver)
        createDatabase(driver)
    }
}

class KoinTestRule : TestWatcher() {
    override fun starting(description: Description) {
        startKoin {
            modules(
                databaseDaoModule,
                repositoryDataModule,
                testCoroutineDispatchersModule,
                testDatabaseModule,
            )
        }
    }

    override fun finished(description: Description) {
        stopKoin()
    }
}
