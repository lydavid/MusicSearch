package ly.david.data.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton
import ly.david.data.BuildConfig
import ly.david.data.coverart.api.CoverArtArchiveApi
import ly.david.data.network.api.MusicBrainzApi
import ly.david.data.spotify.api.SpotifyApi
import ly.david.data.spotify.api.auth.SpotifyAuthApi
import ly.david.data.spotify.api.auth.SpotifyAuthState

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideHttpClient(): HttpClient {
        return ApiHttpClient.create()
    }

    @Singleton
    @Provides
    fun provideCoverArtArchiveApi(
        httpClient: HttpClient,
    ): CoverArtArchiveApi = CoverArtArchiveApi.create(
        httpClient = httpClient,
    )

    @Singleton
    @Provides
    fun provideMusicBrainzApi(
        httpClient: HttpClient,
        musicBrainzAuthState: MusicBrainzAuthState,
    ): MusicBrainzApi = MusicBrainzApi.create(
        httpClient = httpClient,
        musicBrainzAuthState = musicBrainzAuthState,
    )

    @Singleton
    @Provides
    fun provideSpotifyAuthApi(
        httpClient: HttpClient,
    ): SpotifyAuthApi {
        return SpotifyAuthApi.create(
            httpClient = httpClient,
        )
    }

    @Singleton
    @Provides
    fun provideSpotifyApi(
        httpClient: HttpClient,
        spotifyAuthState: SpotifyAuthState,
        spotifyAuthApi: SpotifyAuthApi,
    ): SpotifyApi {
        return SpotifyApi.create(
            httpClient = httpClient,
            clientId = BuildConfig.SPOTIFY_CLIENT_ID,
            clientSecret = BuildConfig.SPOTIFY_CLIENT_SECRET,
            spotifyAuthApi = spotifyAuthApi,
            spotifyAuthState = spotifyAuthState,
        )
    }
}
