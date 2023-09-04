package ly.david.data.di.auth

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import ly.david.data.musicbrainz.auth.MusicBrainzAuthStore
import ly.david.data.spotify.api.auth.SpotifyAuthStore

@InstallIn(SingletonComponent::class)
@Module
internal abstract class AuthBinds {
    @Singleton
    @Binds
    abstract fun provideMusicBrainzAuthStore(bind: MusicBrainzAuthStoreImpl): MusicBrainzAuthStore

    @Singleton
    @Binds
    abstract fun provideSpotifyAuthStore(bind: SpotifyAuthStoreImpl): SpotifyAuthStore
}
