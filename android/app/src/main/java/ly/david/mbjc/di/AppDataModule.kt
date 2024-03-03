package ly.david.mbjc.di

import ly.david.musicsearch.core.models.AppInfo
import ly.david.mbjc.BuildConfig
import org.koin.dsl.module

val appDataModule = module {
    single {
        AppInfo(
            applicationId = BuildConfig.APPLICATION_ID,
        )
    }
}