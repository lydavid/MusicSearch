package ly.david.mbjc

import androidx.test.platform.app.InstrumentationRegistry
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class DatabaseTestRule : TestWatcher() {
    override fun starting(description: Description) {
//        loadKoinModules(databaseModule)
//        loadKoinModules(testDatabaseDriverModule)
//        super.starting(description)
        startKoin {
            androidContext(InstrumentationRegistry.getInstrumentation().targetContext.applicationContext)
            modules(testAndroidAppModule)
        }
    }

    override fun finished(description: Description) {
//        unloadKoinModules(databaseModule)
//        unloadKoinModules(testDatabaseDriverModule)
//        super.finished(description)
//        database.clearAllTables()
        stopKoin()
    }
}
