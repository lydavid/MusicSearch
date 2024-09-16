package ly.david.musicsearch.android.app

import timber.log.Timber

internal class CrashlyticsTree : Timber.Tree() {
    override fun log(
        priority: Int,
        tag: String?,
        message: String,
        t: Throwable?,
    ) {
        // No-op
    }
}
