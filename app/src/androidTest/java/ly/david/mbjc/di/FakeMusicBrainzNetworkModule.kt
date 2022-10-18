package ly.david.mbjc.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import ly.david.mbjc.data.network.MusicBrainzApiService
import ly.david.mbjc.data.network.TestMusicBrainzApiService

//@TestInstallIn(
//    components = [SingletonComponent::class],
//    replaces = [MusicBrainzNetworkModule::class]
//)
@InstallIn(SingletonComponent::class)
@Module
internal object FakeMusicBrainzNetworkModule {
    @Singleton
    @Provides
    fun provideMusicBrainzApi(): MusicBrainzApiService = TestMusicBrainzApiService()
}
