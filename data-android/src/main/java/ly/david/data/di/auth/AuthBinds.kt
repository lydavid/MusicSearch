package ly.david.data.di.auth

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import ly.david.data.network.MusicBrainzAuthState
import ly.david.data.spotify.api.auth.SpotifyAuthState

@InstallIn(SingletonComponent::class)
@Module
internal abstract class AuthBinds {
    @Singleton
    @Binds
    abstract fun provideMusicBrainzAuthState(bind: MusicBrainzAuthStateImpl): MusicBrainzAuthState

    @Singleton
    @Binds
    abstract fun provideSpotifyOAuth(bind: SpotifyAuthStateImpl): SpotifyAuthState
}
