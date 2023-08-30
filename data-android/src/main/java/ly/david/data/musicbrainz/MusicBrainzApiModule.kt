package ly.david.data.musicbrainz

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import ly.david.data.coverart.api.CoverArtArchiveApi
import ly.david.data.network.api.MusicBrainzApi
import ly.david.data.network.api.MusicBrainzAuthApi

@Module
@InstallIn(SingletonComponent::class)
object MusicBrainzApiModule {

    @Singleton
    @Provides
    fun provideCoverArtArchiveApi(): CoverArtArchiveApi = CoverArtArchiveApi.create()

    @Singleton
    @Provides
    fun provideMusicBrainzApi(): MusicBrainzApi = MusicBrainzApi.create()

    @Singleton
    @Provides
    fun provideMusicBrainzAuthApi(): MusicBrainzAuthApi = MusicBrainzAuthApi.create()
}
