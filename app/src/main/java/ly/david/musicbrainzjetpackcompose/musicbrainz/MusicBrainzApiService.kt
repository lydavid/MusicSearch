package ly.david.musicbrainzjetpackcompose.musicbrainz

import ly.david.musicbrainzjetpackcompose.common.JsonUtils
import ly.david.musicbrainzjetpackcompose.common.ServiceUtils
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
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

//    @GET("release")
//    suspend fun browseReleasesByReleaseGroup(@Query("release-group") releaseGroupId: String): ReleaseGroupResponse
    // endregion

    // region lookup

    // TODO: watch out for: Note that the number of linked entities returned is always limited to 25. If you need the remaining results, you will have to perform a browse request.
    @GET("release-group/{releaseGroupId}")
    suspend fun lookupReleaseGroup(
        @Path("releaseGroupId") releaseGroupId: String,
        @Query("inc") include: String = "releases+artists+media"
    ): ReleaseGroup
    // endregion

    companion object {
        private val client = OkHttpClient().newBuilder()
            .addInterceptor(ServiceUtils.interceptor)
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


