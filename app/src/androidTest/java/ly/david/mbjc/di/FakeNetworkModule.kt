package ly.david.mbjc.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton
import ly.david.data.coverart.api.CoverArtArchiveApi
import ly.david.data.di.network.NetworkModule
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.data.spotify.api.SpotifyApi
import ly.david.data.spotify.api.auth.SpotifyOAuthApi
import ly.david.data.spotify.api.auth.SpotifyOAuthClientCredentialsResponse
import ly.david.data.test.api.FakeCoverArtArchiveApi
import ly.david.data.test.api.FakeMusicBrainzApi
import ly.david.data.test.api.FakeSpotifyApi

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [NetworkModule::class]
)
object FakeNetworkModule {

    @Singleton
    @Provides
    fun provideCoverArtArchiveApi(): CoverArtArchiveApi = FakeCoverArtArchiveApi()

    @Singleton
    @Provides
    fun provideMusicBrainzApi(): MusicBrainzApi = FakeMusicBrainzApi()

    @Singleton
    @Provides
    fun provideSpotifyAuthApi(): SpotifyOAuthApi {
        return object : SpotifyOAuthApi {
            override suspend fun getAccessToken(
                clientId: String,
                clientSecret: String,
                grantType: String,
            ): SpotifyOAuthClientCredentialsResponse {
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
