package ly.david.mbjc.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import ly.david.mbjc.data.network.MusicBrainzApiService
import ly.david.mbjc.data.network.MusicBrainzApiServiceImpl
import ly.david.mbjc.data.network.NetworkUtils
import ly.david.mbjc.data.network.coverart.CoverArtArchiveApiService
import okhttp3.OkHttpClient

@InstallIn(SingletonComponent::class)
@Module
internal object NetworkModule {
    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient().newBuilder()
            .addInterceptor(NetworkUtils.interceptor)
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
