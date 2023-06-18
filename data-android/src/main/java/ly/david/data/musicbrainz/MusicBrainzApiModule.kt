package ly.david.data.musicbrainz

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import ly.david.data.coverart.api.CoverArtArchiveApiService
import ly.david.data.coverart.api.CoverArtArchiveApiServiceImpl
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.network.api.MusicBrainzApiServiceImpl
import ly.david.data.network.api.MusicBrainzAuthApi
import ly.david.data.network.api.MusicBrainzAuthApiImpl
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object MusicBrainzApiModule {

    @Singleton
    @Provides
    fun provideCoverArtArchiveApi(
        builder: Retrofit.Builder
    ): CoverArtArchiveApiService = CoverArtArchiveApiServiceImpl.create(builder)

    @Singleton
    @Provides
    fun provideMusicBrainzApi(
        builder: Retrofit.Builder
    ): MusicBrainzApiService = MusicBrainzApiServiceImpl.create(builder)

    @Singleton
    @Provides
    fun provideMusicBrainzAuthApi(
        builder: Retrofit.Builder
    ): MusicBrainzAuthApi = MusicBrainzAuthApiImpl.create(builder)
}
