package ly.david.mbjc

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import timber.log.Timber

class TestApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.d("onCreate")
        startKoin {
            androidContext(this@TestApplication)
            modules(testAndroidAppModule)
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        Timber.d("onTerminate")
        stopKoin()
    }
}
