package ly.david.musicsearch.core.logging

import org.koin.core.module.Module
import org.koin.dsl.module
import timber.log.Timber

actual val loggingModule: Module = module {
    single<Logger> {
        object : Logger {
            override fun d(text: String) {
                Timber.d(text)
            }

            override fun e(exception: Exception) {
                Timber.e(exception)
            }
        }
    }
    // CrashReporter will be implemented by the android:app module because it will vary between flavors.
}
