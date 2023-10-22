package ly.david.mbjc

import android.app.Application
import coil.Coil
import coil.ImageLoaderFactory
import com.google.firebase.crashlytics.FirebaseCrashlytics
import ly.david.mbjc.di.appDataModule
import ly.david.musicsearch.shared.di.sharedModule
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module
import timber.log.Timber

internal class MusicSearchApplication : Application() {

    private val imageLoaderFactory: ImageLoaderFactory by inject()

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashlyticsTree(FirebaseCrashlytics.getInstance()))
        }

        startKoin {
            androidLogger()
            androidContext(this@MusicSearchApplication)
            modules(
                sharedModule,
                // TODO: extract out of :app
                appDataModule,
                ViewModelsModule().module,
            )
        }

        Coil.setImageLoader(imageLoaderFactory)
    }
}

private class CrashlyticsTree(
    private val firebaseCrashlytics: FirebaseCrashlytics,
) : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        firebaseCrashlytics.log(message)
    }
}
