package ly.david.musicsearch.data.repository.releasegroup

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import ly.david.musicsearch.core.coroutines.CoroutineDispatchers
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.createDatabase
import ly.david.musicsearch.data.database.databaseDaoModule
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import java.util.Properties

// val testAndroidAppModule = module {
//    includes(
//        databaseDaoModule,
//    )
// }
//
class KoinTestRule : TestWatcher() {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun starting(description: Description) {
        startKoin {
//            androidContext(InstrumentationRegistry.getInstrumentation().targetContext.applicationContext)
            modules(
                databaseDaoModule,
                module {
                    factory {
                        val testDispatcher = UnconfinedTestDispatcher()
                        CoroutineDispatchers(
                            default = testDispatcher,
                            io = testDispatcher,
                        )
                    }
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
                },
            )
        }
    }

    override fun finished(description: Description) {
        stopKoin()
    }
}