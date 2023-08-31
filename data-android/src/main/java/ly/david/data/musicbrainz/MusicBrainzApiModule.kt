package ly.david.data.musicbrainz

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import ly.david.data.coverart.api.CoverArtArchiveApi
import ly.david.data.network.MusicBrainzAuthState
import ly.david.data.network.api.MusicBrainzApi

@Module
@InstallIn(SingletonComponent::class)
object MusicBrainzApiModule {

    @Singleton
    @Provides
    fun provideCoverArtArchiveApi(): CoverArtArchiveApi = CoverArtArchiveApi.create()

    @Singleton
    @Provides
    fun provideMusicBrainzApi(
        musicBrainzAuthState: MusicBrainzAuthState,
    ): MusicBrainzApi = MusicBrainzApi.create(musicBrainzAuthState)
}
