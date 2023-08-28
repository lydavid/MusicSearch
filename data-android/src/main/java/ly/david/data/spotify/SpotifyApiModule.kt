package ly.david.data.spotify

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import ly.david.data.BuildConfig
import ly.david.data.spotify.auth.SpotifyAuthApi
import ly.david.data.spotify.auth.SpotifyOAuth

@Module
@InstallIn(SingletonComponent::class)
object SpotifyApiModule {

    @Singleton
    @Provides
    fun provideSpotifyAuthApi(
//        builder: Retrofit.Builder,
    ): SpotifyAuthApi {
        return SpotifyAuthApi.create()
    }

    @Singleton
    @Provides
    fun provideSpotifyApi(
        spotifyOAuth: SpotifyOAuth,
        spotifyAuthApi: SpotifyAuthApi,
    ): SpotifyApi {
//        val clientBuilder = OkHttpClient().newBuilder()
//
//        if (BuildConfig.DEBUG) {
//            clientBuilder.addInterceptor(httpLoggingInterceptor)
//        }
//
//        clientBuilder.addInterceptor(spotifyAccessTokenInterceptor)

        return SpotifyApi.create(
            clientId = BuildConfig.SPOTIFY_CLIENT_ID,
            clientSecret = BuildConfig.SPOTIFY_CLIENT_SECRET,
            spotifyAuthApi = spotifyAuthApi,
            spotifyOAuth = spotifyOAuth,
        )
    }
}
