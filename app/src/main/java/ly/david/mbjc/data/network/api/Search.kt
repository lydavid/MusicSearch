package ly.david.mbjc.data.network.api

import com.squareup.moshi.Json
import ly.david.mbjc.data.network.AreaMusicBrainzModel
import ly.david.mbjc.data.network.ArtistMusicBrainzModel
import ly.david.mbjc.data.network.EventMusicBrainzModel
import ly.david.mbjc.data.network.InstrumentMusicBrainzModel
import ly.david.mbjc.data.network.LabelMusicBrainzModel
import ly.david.mbjc.data.network.PlaceMusicBrainzModel
import ly.david.mbjc.data.network.RecordingMusicBrainzModel
import ly.david.mbjc.data.network.ReleaseGroupMusicBrainzModel
import ly.david.mbjc.data.network.ReleaseMusicBrainzModel
import ly.david.mbjc.data.network.SeriesMusicBrainzModel
import ly.david.mbjc.data.network.WorkMusicBrainzModel
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

    @GET("work")
    suspend fun queryWorks(
        @Query("query") query: String,
        @Query("limit") limit: Int = SEARCH_BROWSE_LIMIT,
        @Query("offset") offset: Int = 0,
    ): SearchWorksResponse

    @GET("area")
    suspend fun queryAreas(
        @Query("query") query: String,
        @Query("limit") limit: Int = SEARCH_BROWSE_LIMIT,
        @Query("offset") offset: Int = 0,
    ): SearchAreasResponse

    @GET("place")
    suspend fun queryPlaces(
        @Query("query") query: String,
        @Query("limit") limit: Int = SEARCH_BROWSE_LIMIT,
        @Query("offset") offset: Int = 0,
    ): SearchPlacesResponse

    @GET("instrument")
    suspend fun queryInstruments(
        @Query("query") query: String,
        @Query("limit") limit: Int = SEARCH_BROWSE_LIMIT,
        @Query("offset") offset: Int = 0,
    ): SearchInstrumentsResponse

    @GET("label")
    suspend fun queryLabels(
        @Query("query") query: String,
        @Query("limit") limit: Int = SEARCH_BROWSE_LIMIT,
        @Query("offset") offset: Int = 0,
    ): SearchLabelsResponse

    @GET("event")
    suspend fun queryEvents(
        @Query("query") query: String,
        @Query("limit") limit: Int = SEARCH_BROWSE_LIMIT,
        @Query("offset") offset: Int = 0,
    ): SearchEventsResponse

    @GET("series")
    suspend fun querySeries(
        @Query("query") query: String,
        @Query("limit") limit: Int = SEARCH_BROWSE_LIMIT,
        @Query("offset") offset: Int = 0,
    ): SearchSeriesResponse
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

internal data class SearchWorksResponse(
    @Json(name = "count") val count: Int, // Total hits
    @Json(name = "offset") val offset: Int,
    @Json(name = "works") val works: List<WorkMusicBrainzModel>
)

internal data class SearchAreasResponse(
    @Json(name = "count") val count: Int, // Total hits
    @Json(name = "offset") val offset: Int,
    @Json(name = "areas") val areas: List<AreaMusicBrainzModel>
)

internal data class SearchPlacesResponse(
    @Json(name = "count") val count: Int, // Total hits
    @Json(name = "offset") val offset: Int,
    @Json(name = "places") val places: List<PlaceMusicBrainzModel>
)

internal data class SearchInstrumentsResponse(
    @Json(name = "count") val count: Int, // Total hits
    @Json(name = "offset") val offset: Int,
    @Json(name = "instruments") val instruments: List<InstrumentMusicBrainzModel>
)

internal data class SearchLabelsResponse(
    @Json(name = "count") val count: Int, // Total hits
    @Json(name = "offset") val offset: Int,
    @Json(name = "labels") val labels: List<LabelMusicBrainzModel>
)

internal data class SearchEventsResponse(
    @Json(name = "count") val count: Int, // Total hits
    @Json(name = "offset") val offset: Int,
    @Json(name = "events") val events: List<EventMusicBrainzModel>
)

internal data class SearchSeriesResponse(
    @Json(name = "count") val count: Int, // Total hits
    @Json(name = "offset") val offset: Int,
    @Json(name = "series") val series: List<SeriesMusicBrainzModel>
)
