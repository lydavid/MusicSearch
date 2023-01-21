package ly.david.data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Singleton
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.network.api.MusicBrainzApiServiceImpl
import ly.david.data.network.api.coverart.CoverArtArchiveApiService
import ly.david.data.network.api.coverart.CoverArtArchiveApiServiceImpl
import okhttp3.Cache
import okhttp3.OkHttpClient

private const val USER_AGENT = "User-Agent"
private const val USER_AGENT_VALUE = "MusicBrainzJetpackCompose/0.1.0"
private const val ACCEPT = "Accept"
private const val ACCEPT_VALUE = "application/json"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
    ): OkHttpClient {
        return OkHttpClient().newBuilder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader(USER_AGENT, USER_AGENT_VALUE)
                    .addHeader(ACCEPT, ACCEPT_VALUE)
                    .build()
                chain.proceed(request)
            }
            // TODO: make sure when doing swipe to refresh, we actually fetch from network not this cache
            //  right now, it seems to work like that sometimes, switching between 200 and 304
            .cache(
                // TODO: how come we were able to use named parameters before?
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
}
