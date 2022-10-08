package ly.david.mbjc.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Singleton
import ly.david.mbjc.data.network.MusicBrainzApiService
import ly.david.mbjc.data.network.MusicBrainzApiServiceImpl
import ly.david.mbjc.data.network.coverart.CoverArtArchiveApiService
import okhttp3.Cache
import okhttp3.OkHttpClient

private const val USER_AGENT = "User-Agent"
private const val USER_AGENT_VALUE = "MusicBrainzJetpackCompose/0.1.0"
private const val ACCEPT = "Accept"
private const val ACCEPT_VALUE = "application/json"

@InstallIn(SingletonComponent::class)
@Module
internal object NetworkModule {

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
                Cache(
                    directory = File(context.cacheDir, "http_cache"),
                    // $0.05 worth of phone storage in 2020
                    maxSize = 50L * 1024L * 1024L // 50 MiB
                )
            )
            .build()
    }

    @Singleton
    @Provides
    fun provideMusicBrainzApi(
        okHttpClient: OkHttpClient
    ): MusicBrainzApiService = MusicBrainzApiServiceImpl.create(okHttpClient)

    @Singleton
    @Provides
    fun provideCoverArtArchiveApi(
        okHttpClient: OkHttpClient
    ): CoverArtArchiveApiService = CoverArtArchiveApiService.create(okHttpClient)
}
