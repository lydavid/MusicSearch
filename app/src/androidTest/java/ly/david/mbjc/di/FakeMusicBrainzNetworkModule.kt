package ly.david.mbjc.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton
import ly.david.data.base.JsonUtils
import ly.david.data.coverart.api.CoverArtArchiveApiService
import ly.david.data.di.MusicBrainzNetworkModule
import ly.david.data.network.api.FakeMusicBrainzApiService
import ly.david.data.network.api.MusicBrainzApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

internal const val TEST_PORT = 8080

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [MusicBrainzNetworkModule::class]
)
internal object FakeMusicBrainzNetworkModule {

    @Singleton
    @Provides
    fun provideCoverArtArchiveApi(
        okHttpClient: OkHttpClient
    ): CoverArtArchiveApiService {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(JsonUtils.moshi))
            .client(okHttpClient)
            .baseUrl("https://localhost:$TEST_PORT")
            .build()

        return retrofit.create(CoverArtArchiveApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideMusicBrainzApi(): MusicBrainzApiService = FakeMusicBrainzApiService()
}
