package ly.david.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import ly.david.data.auth.MusicBrainzAuthState
import ly.david.data.auth.MusicBrainzAuthStateImpl

@InstallIn(SingletonComponent::class)
@Module
internal abstract class MusicBrainzAuthStateBinds {
    @Singleton
    @Binds
    abstract fun provideMusicBrainzAuthState(bind: MusicBrainzAuthStateImpl): MusicBrainzAuthState
}
