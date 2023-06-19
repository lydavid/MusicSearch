package ly.david.data.spotify

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import ly.david.data.BuildConfig
import ly.david.data.spotify.auth.SpotifyAuthApi
import ly.david.data.spotify.auth.SpotifyAuthApiImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object SpotifyApiModule {

    @Singleton
    @Provides
    fun provideSpotifyAuthApi(
        builder: Retrofit.Builder
    ): SpotifyAuthApi {
        return SpotifyAuthApiImpl.create(builder)
    }

    @Singleton
    @Provides
    fun provideSpotifyApi(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        spotifyAccessTokenInterceptor: SpotifyAccessTokenInterceptor
    ): SpotifyApi {

        val clientBuilder = OkHttpClient().newBuilder()

        if (BuildConfig.DEBUG) {
            clientBuilder.addInterceptor(httpLoggingInterceptor)
        }

        clientBuilder.addInterceptor(spotifyAccessTokenInterceptor)

        return SpotifyApiImpl.create(clientBuilder.build())
    }
}
