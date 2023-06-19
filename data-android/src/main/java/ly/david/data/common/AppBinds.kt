package ly.david.data.common

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import ly.david.data.musicbrainz.MusicBrainzAuthState
import ly.david.data.musicbrainz.MusicBrainzAuthStateImpl
import ly.david.data.spotify.SpotifyOAuth
import ly.david.data.spotify.SpotifyOAuthImpl

@InstallIn(SingletonComponent::class)
@Module
internal abstract class AppBinds {
    @Singleton
    @Binds
    abstract fun provideMusicBrainzAuthState(bind: MusicBrainzAuthStateImpl): MusicBrainzAuthState

    @Singleton
    @Binds
    abstract fun provideSpotifyOAuth(bind: SpotifyOAuthImpl): SpotifyOAuth
}
