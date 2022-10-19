package ly.david.mbjc.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton
import ly.david.mbjc.data.network.api.FakeMusicBrainzApiService
import ly.david.mbjc.data.network.api.MusicBrainzApiService

@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [MusicBrainzNetworkModule::class]
)
@Module
internal object FakeMusicBrainzNetworkModule {
    @Singleton
    @Provides
    fun provideMusicBrainzApi(): MusicBrainzApiService = FakeMusicBrainzApiService()
}
