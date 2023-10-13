package ly.david.data.di.logging

import ly.david.musicsearch.data.core.logging.Logger
import org.koin.dsl.module
import timber.log.Timber

val loggingModule = module {
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
}
