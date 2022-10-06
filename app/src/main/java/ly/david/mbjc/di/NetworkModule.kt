package ly.david.mbjc.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import ly.david.mbjc.data.network.MusicBrainzApiService
import ly.david.mbjc.data.network.MusicBrainzApiServiceImpl
import ly.david.mbjc.data.network.coverart.CoverArtArchiveApiService

@InstallIn(SingletonComponent::class)
@Module
internal object NetworkModule {
    @Singleton
    @Provides
    fun provideMusicBrainzApi(): MusicBrainzApiService = MusicBrainzApiServiceImpl.create()

    @Singleton
    @Provides
    fun provideCoverArtArchiveApi(): CoverArtArchiveApiService = CoverArtArchiveApiService.create()
}
