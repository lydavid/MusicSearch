package ly.david.mbjc

import androidx.test.platform.app.InstrumentationRegistry
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module

class KoinTestRule(
//    private val context: Context,
    private val modules: List<Module>,
) : TestWatcher() {
    override fun starting(description: Description) {
        startKoin {
            androidContext(InstrumentationRegistry.getInstrumentation().targetContext.applicationContext)
//            androidContext(context)
            modules(modules)
        }
    }

    override fun finished(description: Description) {
        stopKoin()
    }
}
