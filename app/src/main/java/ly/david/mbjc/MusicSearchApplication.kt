package ly.david.mbjc

import android.app.Application
import coil.Coil
import coil.ImageLoaderFactory
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import timber.log.Timber

@HiltAndroidApp
internal class MusicSearchApplication : Application() {

    @Inject
    lateinit var imageLoaderFactory: ImageLoaderFactory

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashlyticsTree(FirebaseCrashlytics.getInstance()))
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
