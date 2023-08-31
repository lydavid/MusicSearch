package ly.david.data.spotify

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import ly.david.data.BuildConfig
import ly.david.data.spotify.api.SpotifyApi
import ly.david.data.spotify.api.auth.SpotifyAuthApi
import ly.david.data.spotify.api.auth.SpotifyAuthState

@Module
@InstallIn(SingletonComponent::class)
object SpotifyApiModule {

    @Singleton
    @Provides
    fun provideSpotifyAuthApi(): SpotifyAuthApi {
        return SpotifyAuthApi.create()
    }

    @Singleton
    @Provides
    fun provideSpotifyApi(
        spotifyAuthState: SpotifyAuthState,
        spotifyAuthApi: SpotifyAuthApi,
    ): SpotifyApi {
        return SpotifyApi.create(
            clientId = BuildConfig.SPOTIFY_CLIENT_ID,
            clientSecret = BuildConfig.SPOTIFY_CLIENT_SECRET,
            spotifyAuthApi = spotifyAuthApi,
            spotifyAuthState = spotifyAuthState,
        )
    }
}
