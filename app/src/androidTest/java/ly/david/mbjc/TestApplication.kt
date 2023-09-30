package ly.david.mbjc

import android.app.Application

class TestApplication : Application() {
    override fun onCreate() {
        super.onCreate()
//        startKoin {
//            androidContext(this@TestApplication)
//            modules(testAndroidAppModule)
//        }
    }

    override fun onTerminate() {
        super.onTerminate()
//        stopKoin()
    }
}
