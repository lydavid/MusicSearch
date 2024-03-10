package ly.david.mbjc.di

import ly.david.musicsearch.core.models.AppInfo
import ly.david.mbjc.BuildConfig
import ly.david.mbjc.ui.TopLevelViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val androidAppModule = module {
    single {
        AppInfo(
            applicationId = BuildConfig.APPLICATION_ID,
        )
    }
    viewModelOf(::TopLevelViewModel)
}
