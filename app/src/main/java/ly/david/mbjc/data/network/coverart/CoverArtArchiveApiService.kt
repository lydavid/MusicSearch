package ly.david.mbjc.data.network.coverart

import ly.david.mbjc.data.network.JsonUtils
import ly.david.mbjc.data.network.NetworkUtils
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

private const val MUSIC_BRAINZ_BASE_URL = "https://coverartarchive.org/"


internal interface CoverArtArchiveApiService {

    @GET("release-group/{id}")
    suspend fun getReleaseGroupCoverArts(@Path("id") releaseGroupId: String): CoverArtsResponse

    /**
     * This is used to get the URLs for this release. So after calling this, we need to make another API call
     * based on the retrieved URLs.
     */
    @GET("release/{id}")
    suspend fun getReleaseCoverArts(@Path("id") releaseId: String): CoverArtsResponse

    companion object {

        private val client = OkHttpClient().newBuilder()
            .addInterceptor(NetworkUtils.interceptor)
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
