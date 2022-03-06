package ly.david.mbjc.data.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import ly.david.mbjc.common.JsonUtils
import ly.david.mbjc.common.ServiceUtils
import ly.david.mbjc.data.browse.Browse
import ly.david.mbjc.data.lookup.Lookup
import ly.david.mbjc.data.search.Search
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val MUSIC_BRAINZ_API_BASE_URL = "$MUSIC_BRAINZ_BASE_URL/ws/2/"

// TODO: can put this into di module
interface MusicBrainzApiService : Search, Browse, Lookup {
    companion object {
        private val client = OkHttpClient().newBuilder()
            .addInterceptor(ServiceUtils.interceptor)
            // TODO: should process 503 errors, then we should display alert saying we ran into it (rate limited etc)
            .build()

        fun create(): MusicBrainzApiService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create(JsonUtils.moshi))
                .client(client)
                .baseUrl(MUSIC_BRAINZ_API_BASE_URL)
                .build()

            return retrofit.create(MusicBrainzApiService::class.java)
        }
    }
}

@InstallIn(SingletonComponent::class)
@Module
object MusicBrainzApiModule {
    @Singleton
    @Provides
    fun provideMusicBrainzApi(): MusicBrainzApiService = MusicBrainzApiService.create()
}
