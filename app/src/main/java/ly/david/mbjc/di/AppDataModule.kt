package ly.david.mbjc.di

import ly.david.data.core.AppInfo
import ly.david.mbjc.BuildConfig
import org.koin.dsl.module

//@InstallIn(SingletonComponent::class)
//@Module
//object AppDataModule {
//
//    @Provides
//    fun provideAppData(): AppInfo {
//        return AppInfo(
//            applicationId = BuildConfig.APPLICATION_ID
//        )
//    }
//}

val appDataModule = module {
    single {
        AppInfo(
            applicationId = BuildConfig.APPLICATION_ID
        )
    }
}
