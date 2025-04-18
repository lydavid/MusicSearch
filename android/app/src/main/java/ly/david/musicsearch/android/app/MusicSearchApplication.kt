package ly.david.musicsearch.android.app

import android.app.Application
import ly.david.musicsearch.shared.di.sharedModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

internal class MusicSearchApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.FLAVOR == "fDroid") {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashlyticsTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@MusicSearchApplication)
            modules(
                sharedModule,
            )
        }
    }
}
