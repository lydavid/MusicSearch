package ly.david.mbjc.data.network

import com.squareup.moshi.Json
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Search for MusicBrainz entities using text.
 */
internal interface Search {

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

    @GET("release")
    suspend fun queryReleases(
        @Query("query") query: String,
        @Query("limit") limit: Int = SEARCH_BROWSE_LIMIT,
        @Query("offset") offset: Int = 0,
    ): SearchReleasesResponse

    @GET("recording")
    suspend fun queryRecordings(
        @Query("query") query: String,
        @Query("limit") limit: Int = SEARCH_BROWSE_LIMIT,
        @Query("offset") offset: Int = 0,
    ): SearchRecordingsResponse

    @GET("place")
    suspend fun queryPlaces(
        @Query("query") query: String,
        @Query("limit") limit: Int = SEARCH_BROWSE_LIMIT,
        @Query("offset") offset: Int = 0,
    ): SearchPlacesResponse
}

internal data class SearchArtistsResponse(
    @Json(name = "count") val count: Int, // Total hits
    @Json(name = "offset") val offset: Int,
    @Json(name = "artists") val artists: List<ArtistMusicBrainzModel>
)

internal data class SearchReleaseGroupsResponse(
    @Json(name = "count") val count: Int, // Total hits
    @Json(name = "offset") val offset: Int,
    @Json(name = "release-groups") val releaseGroups: List<ReleaseGroupMusicBrainzModel>
)

internal data class SearchReleasesResponse(
    @Json(name = "count") val count: Int, // Total hits
    @Json(name = "offset") val offset: Int,
    @Json(name = "releases") val releases: List<ReleaseMusicBrainzModel>
)

internal data class SearchRecordingsResponse(
    @Json(name = "count") val count: Int, // Total hits
    @Json(name = "offset") val offset: Int,
    @Json(name = "recordings") val recordings: List<RecordingMusicBrainzModel>
)

internal data class SearchPlacesResponse(
    @Json(name = "count") val count: Int, // Total hits
    @Json(name = "offset") val offset: Int,
    @Json(name = "places") val places: List<PlaceMusicBrainzModel>
)
