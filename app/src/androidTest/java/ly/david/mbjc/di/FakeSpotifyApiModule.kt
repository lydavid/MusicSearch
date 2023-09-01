package ly.david.mbjc.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton
import ly.david.data.network.api.FakeSpotifyApi
import ly.david.data.spotify.SpotifyApiModule
import ly.david.data.spotify.api.SpotifyApi
import ly.david.data.spotify.api.auth.SpotifyAccessToken
import ly.david.data.spotify.api.auth.SpotifyAuthApi

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [SpotifyApiModule::class]
)
object FakeSpotifyApiModule {

    @Singleton
    @Provides
    fun provideSpotifyAuthApi(): SpotifyAuthApi {
        return object : SpotifyAuthApi {
            override suspend fun getAccessToken(
                clientId: String,
                clientSecret: String,
                grantType: String,
            ): SpotifyAccessToken {
                TODO("Not yet implemented")
            }
        }
    }

    @Singleton
    @Provides
    fun provideSpotifyApi(): SpotifyApi {
        return FakeSpotifyApi()
    }
}
