package ly.david.musicsearch.core.logging

import co.touchlab.crashkios.crashlytics.CrashlyticsKotlin
import ly.david.musicsearch.core.logging.crash.CrashReporterSettings
import ly.david.musicsearch.core.logging.crash.NoOpCrashReporterSettings
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val loggingModule: Module = module {
    single<Logger> {
        object : Logger {
            override fun d(text: String) {
                println(text)
            }

            override fun e(exception: Exception) {
                CrashlyticsKotlin.sendHandledException(exception)
            }
        }
    }
    singleOf(::NoOpCrashReporterSettings) bind CrashReporterSettings::class
}
