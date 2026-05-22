package ly.david.musicsearch.android.app

import ly.david.musicsearch.core.logging.crash.CrashReporterSettings
import ly.david.musicsearch.shared.domain.app.AndroidBuildConfig
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val androidModule = module {
    singleOf(::CrashlyticsCrashReporterSettings) bind CrashReporterSettings::class
    single {
        AndroidBuildConfig(
            isDebug = BuildConfig.DEBUG,
        )
    }
}
