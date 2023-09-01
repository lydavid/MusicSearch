package ly.david.data.spotify

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.engine.HttpClientEngine
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
    fun provideSpotifyAuthApi(
        engine: HttpClientEngine,
    ): SpotifyAuthApi {
        return SpotifyAuthApi.create(engine)
    }

    @Singleton
    @Provides
    fun provideSpotifyApi(
        engine: HttpClientEngine,
        spotifyAuthState: SpotifyAuthState,
        spotifyAuthApi: SpotifyAuthApi,
    ): SpotifyApi {
        return SpotifyApi.create(
            engine = engine,
            clientId = BuildConfig.SPOTIFY_CLIENT_ID,
            clientSecret = BuildConfig.SPOTIFY_CLIENT_SECRET,
            spotifyAuthApi = spotifyAuthApi,
            spotifyAuthState = spotifyAuthState,
        )
    }
}
