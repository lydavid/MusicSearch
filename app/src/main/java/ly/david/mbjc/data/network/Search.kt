package ly.david.mbjc.data.network

import com.squareup.moshi.Json
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Search for MusicBrainz entities using text.
 */
interface Search {

    @GET("artist")
    suspend fun queryArtists(
        @Query("query") query: String,
        @Query("limit") limit: Int = SEARCH_BROWSE_LIMIT,
        @Query("offset") offset: Int = 0,
    ): SearchArtistsResponse

    @GET("release-group")
    suspend fun queryReleaseGroups(
        @Query("query") query: String,
        @Query("limit") limit: Int = SEARCH_BROWSE_LIMIT,
        @Query("offset") offset: Int = 0,
    ): SearchReleaseGroupsResponse
}

data class SearchArtistsResponse(
    @Json(name = "count") val count: Int, // Total hits
    @Json(name = "offset") val offset: Int,
    @Json(name = "artists") val artists: List<ArtistMusicBrainzModel>
)

data class SearchReleaseGroupsResponse(
    @Json(name = "count") val count: Int, // Total hits
    @Json(name = "offset") val offset: Int,
    @Json(name = "release-groups") val releaseGroups: List<ReleaseGroupMusicBrainzModel>
)
