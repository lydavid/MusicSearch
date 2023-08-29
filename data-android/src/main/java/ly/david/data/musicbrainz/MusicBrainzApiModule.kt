package ly.david.data.musicbrainz

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import ly.david.data.coverart.api.CoverArtArchiveApi
import ly.david.data.network.api.MusicBrainzApi
import ly.david.data.network.api.MusicBrainzApiImpl
import ly.david.data.network.api.MusicBrainzAuthApi
import ly.david.data.network.api.MusicBrainzAuthApiImpl
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object MusicBrainzApiModule {

    @Singleton
    @Provides
    fun provideCoverArtArchiveApi(): CoverArtArchiveApi = CoverArtArchiveApi.create()

    @Singleton
    @Provides
    fun provideMusicBrainzApi(
        builder: Retrofit.Builder,
    ): MusicBrainzApi = MusicBrainzApiImpl.create(builder)

    @Singleton
    @Provides
    fun provideMusicBrainzAuthApi(
        builder: Retrofit.Builder,
    ): MusicBrainzAuthApi = MusicBrainzAuthApiImpl.create(builder)
}
