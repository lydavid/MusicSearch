package ly.david.mbjc

import android.app.Application
import coil.Coil
import coil.ImageLoaderFactory
import com.google.firebase.crashlytics.FirebaseCrashlytics
import ly.david.musicsearch.data.database.dao.EventDao
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

internal class MusicSearchApplication : Application() {

    private val imageLoaderFactory: ImageLoaderFactory by inject()
    private val eventDao: EventDao by inject()

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
            modules(androidAppModule)
        }

        try {
//            eventDao.insert(
//                Event("z", "hello", "", "", "", "", false)
//            )
            val e=eventDao.getEvent("e")
            Timber.d("${e.name}")
        } catch (ex: Throwable) {
            Timber.e("oof")
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
