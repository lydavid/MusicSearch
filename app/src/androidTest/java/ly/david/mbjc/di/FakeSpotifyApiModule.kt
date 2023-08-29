package ly.david.mbjc.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton
import ly.david.data.network.api.FakeSpotifyApi
import ly.david.data.spotify.SpotifyApiModule
import ly.david.data.spotify.api.SpotifyApi

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [SpotifyApiModule::class]
)
object FakeSpotifyApiModule {

    @Singleton
    @Provides
    fun provideSpotifyApi(): SpotifyApi {
        return FakeSpotifyApi()
    }
}
