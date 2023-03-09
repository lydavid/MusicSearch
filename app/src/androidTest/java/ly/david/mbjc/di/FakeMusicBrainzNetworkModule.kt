package ly.david.mbjc.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton
import ly.david.data.coverart.api.CoverArtArchiveApiService
import ly.david.data.di.MusicBrainzNetworkModule
import ly.david.data.network.api.FakeMusicBrainzApiService
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.network.api.MusicBrainzAuthApi
import retrofit2.Retrofit

internal const val TEST_PORT = 8080
private const val TEST_BASE_URL = "https://localhost:$TEST_PORT"

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [MusicBrainzNetworkModule::class]
)
internal object FakeMusicBrainzNetworkModule {

    @Singleton
    @Provides
    fun provideCoverArtArchiveApi(
        builder: Retrofit.Builder
    ): CoverArtArchiveApiService {
        val retrofit = builder
            .baseUrl(TEST_BASE_URL)
            .build()

        return retrofit.create(CoverArtArchiveApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideMusicBrainzApi(): MusicBrainzApiService = FakeMusicBrainzApiService()

    @Singleton
    @Provides
    fun provideMusicBrainzAuthApi(
        builder: Retrofit.Builder
    ): MusicBrainzAuthApi {
        val retrofit = builder
            .baseUrl(TEST_BASE_URL)
            .build()

        return retrofit.create(MusicBrainzAuthApi::class.java)
    }
}
