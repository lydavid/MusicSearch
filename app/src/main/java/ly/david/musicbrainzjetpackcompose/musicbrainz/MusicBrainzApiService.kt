package ly.david.musicbrainzjetpackcompose.musicbrainz

import ly.david.musicbrainzjetpackcompose.common.JsonUtils
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val MUSIC_BRAINZ_BASE_URL = "https://musicbrainz.org/ws/2/"

internal interface MusicBrainzApiService {

    // region search
    @GET("artist")
    suspend fun queryArtists(@Query("query") query: String): SearchArtistsResponse
    // endregion

    // region browse
    @GET("release-group")
    suspend fun browseReleaseGroupsByArtist(@Query("artist") artistId: String): BrowseReleaseGroupsResponse

    @GET("release")
    suspend fun browseReleasesByReleaseGroup(@Query("release-group") releaseGroupId: String): BrowseReleasesResponse
    // endregion

    companion object {

        private val interceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("User-Agent", "MusicBrainzJetpackCompose/0.1.0")
                .addHeader("Accept", "application/json")
                // TODO: response in App Inspection is gibberish, is there a header we need to add?
                .build()
            chain.proceed(request)
        }

        private val client = OkHttpClient().newBuilder()
            .addInterceptor(interceptor)
            // TODO: should process 503 errors, then we should display alert saying we ran into it (rate limited etc)
            .build()

        fun create(): MusicBrainzApiService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create(JsonUtils.moshi))
                .client(client)
                .baseUrl(MUSIC_BRAINZ_BASE_URL)
                .build()

            return retrofit.create(MusicBrainzApiService::class.java)
        }
    }
}


