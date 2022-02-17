package ly.david.musicbrainzjetpackcompose.coverartarchive

import ly.david.musicbrainzjetpackcompose.common.JsonUtils
import ly.david.musicbrainzjetpackcompose.common.ServiceUtils
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

private const val MUSIC_BRAINZ_BASE_URL = "https://coverartarchive.org/"

internal interface CoverArtArchiveApiService {

    @GET("release-group/{id}")
    suspend fun getCoverArts(@Path("id") releaseGroupId: String): CoverArtsResponse

    companion object {

        private val client = OkHttpClient().newBuilder()
            .addInterceptor(ServiceUtils.interceptor)
            .build()

        fun create(): CoverArtArchiveApiService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create(JsonUtils.moshi))
                .client(client)
                .baseUrl(MUSIC_BRAINZ_BASE_URL)
                .build()

            return retrofit.create(CoverArtArchiveApiService::class.java)
        }
    }
}


