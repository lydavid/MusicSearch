package ly.david.mbjc.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton
import ly.david.data.coverart.api.CoverArtArchiveApi
import ly.david.data.musicbrainz.MusicBrainzApiModule
import ly.david.data.network.api.FakeCoverArtArchiveApi
import ly.david.data.network.api.FakeMusicBrainzApi
import ly.david.data.network.api.MusicBrainzApi

internal const val TEST_PORT = 8080
private const val TEST_BASE_URL = "https://localhost:$TEST_PORT"

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [MusicBrainzApiModule::class]
)
internal object FakeMusicBrainzApiModule {

    @Singleton
    @Provides
    fun provideCoverArtArchiveApi(): CoverArtArchiveApi = FakeCoverArtArchiveApi()

    @Singleton
    @Provides
    fun provideMusicBrainzApi(): MusicBrainzApi = FakeMusicBrainzApi()
}
