package ly.david.mbjc.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ly.david.data.core.AppInfo
import ly.david.mbjc.BuildConfig

@InstallIn(SingletonComponent::class)
@Module
object AppDataModule {

    @Provides
    fun provideAppData(): AppInfo {
        return AppInfo(
            applicationId = BuildConfig.APPLICATION_ID
        )
    }
}
