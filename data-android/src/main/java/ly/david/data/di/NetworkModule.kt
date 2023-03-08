package ly.david.data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Singleton
import kotlinx.coroutines.runBlocking
import ly.david.data.auth.MusicBrainzAuthState
import ly.david.data.coverart.api.CoverArtArchiveApiService
import ly.david.data.coverart.api.CoverArtArchiveApiServiceImpl
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.network.api.MusicBrainzApiServiceImpl
import ly.david.data.network.api.MusicBrainzLogoutApi
import ly.david.data.network.api.MusicBrainzUserApi
import ly.david.data.network.api.MusicBrainzUserApiImpl
import okhttp3.Cache
import okhttp3.OkHttpClient

private const val USER_AGENT = "User-Agent"
private const val USER_AGENT_VALUE = "MusicSearch/0.2.1 (https://github.com/lydavid/MusicSearch)"
private const val ACCEPT = "Accept"
private const val ACCEPT_VALUE = "application/json"
private const val AUTHORIZATION = "Authorization"
private const val BEARER = "Bearer"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        musicBrainzAuthState: MusicBrainzAuthState
    ): OkHttpClient {
        return OkHttpClient().newBuilder()
            .addInterceptor { chain ->
                val request = chain.request()
                val requestBuilder = request.newBuilder()
                    .addHeader(USER_AGENT, USER_AGENT_VALUE)
                    .addHeader(ACCEPT, ACCEPT_VALUE)

                // TODO: don't always want to pass this to every request
                runBlocking {
                    val authState = musicBrainzAuthState.getAuthState()
                    val token = authState?.accessToken
                    if (token != null) {
                        requestBuilder.addHeader(AUTHORIZATION, "$BEARER $token")
                    }
                }

                chain.proceed(requestBuilder.build())
            }
            // TODO: make sure when doing swipe to refresh, we actually fetch from network not this cache
            //  right now, it seems to work like that sometimes, switching between 200 and 304
            .cache(
                // TODO: how come we were able to use named parameters before?
                //  seems like we're only able to access "java" classes here whereas in app, we could access kotlin
                //  notice how we need to use url() vs url
                Cache(
                    File(context.cacheDir, "http_cache"),
                    // $0.05 worth of phone storage in 2020
                    50L * 1024L * 1024L // 50 MiB
                )
            )
            .build()
    }
}

@Module
@InstallIn(SingletonComponent::class)
object MusicBrainzNetworkModule {

    @Singleton
    @Provides
    fun provideCoverArtArchiveApi(
        okHttpClient: OkHttpClient
    ): CoverArtArchiveApiService = CoverArtArchiveApiServiceImpl.create(okHttpClient)

    @Singleton
    @Provides
    fun provideMusicBrainzApi(
        okHttpClient: OkHttpClient
    ): MusicBrainzApiService = MusicBrainzApiServiceImpl.create(okHttpClient)

    @Singleton
    @Provides
    fun provideMusicBrainzUserApi(
        okHttpClient: OkHttpClient
    ): MusicBrainzUserApi = MusicBrainzUserApiImpl.create(okHttpClient)

    @Singleton
    @Provides
    fun provideMusicBrainzLogoutApi(
    ): MusicBrainzLogoutApi = MusicBrainzLogoutApi.create()
}
