package ly.david.musicsearch.android.app

import ly.david.musicsearch.core.models.AppInfo
import org.koin.dsl.module

val androidAppModule = module {
    single {
        AppInfo(
            applicationId = BuildConfig.APPLICATION_ID,
        )
    }
}
