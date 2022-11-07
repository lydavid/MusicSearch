package ly.david.mbjc.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton
import ly.david.data.di.MusicBrainzNetworkModule
import ly.david.data.network.api.FakeCoverArtArchiveApiService
import ly.david.data.network.api.FakeMusicBrainzApiService
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.network.api.coverart.CoverArtArchiveApiService

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [MusicBrainzNetworkModule::class]
)
internal object FakeMusicBrainzNetworkModule {

    @Singleton
    @Provides
    fun provideCoverArtArchiveApi(): CoverArtArchiveApiService = FakeCoverArtArchiveApiService()

    @Singleton
    @Provides
    fun provideMusicBrainzApi(): MusicBrainzApiService = FakeMusicBrainzApiService()
}
