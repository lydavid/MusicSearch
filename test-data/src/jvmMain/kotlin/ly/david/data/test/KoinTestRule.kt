package ly.david.data.test

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.createDatabase
import ly.david.musicsearch.data.database.databaseDaoModule
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import java.util.Properties

@OptIn(ExperimentalCoroutinesApi::class)
private val testCoroutineDispatchersModule = module {
    single {
        val testDispatcher = UnconfinedTestDispatcher()
        CoroutineDispatchers(
            default = testDispatcher,
            io = testDispatcher,
        )
    }
}
private val testDatabaseModule = module {
    single<Database> {
        val driver = JdbcSqliteDriver(
            url = JdbcSqliteDriver.IN_MEMORY,
            schema = Database.Schema,
            properties = Properties().apply {
                put(
                    "foreign_keys",
                    "true",
                )
            },
        )
        createDatabase(driver)
    }
}

class KoinTestRule : TestWatcher() {
    override fun starting(description: Description) {
        startKoin {
            modules(
                testCoroutineDispatchersModule,
                databaseDaoModule,
                testDatabaseModule,
            )
        }
    }

    override fun finished(description: Description) {
        stopKoin()
    }
}
