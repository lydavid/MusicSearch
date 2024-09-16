package ly.david.musicsearch.android.app

import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

internal class CrashlyticsTree : Timber.Tree() {
    override fun log(
        priority: Int,
        tag: String?,
        message: String,
        t: Throwable?,
    ) {
        FirebaseCrashlytics.getInstance().log(message)
    }
}
