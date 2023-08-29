package ly.david.data.network.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.appendPathSegments
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ly.david.data.network.AreaMusicBrainzModel
import ly.david.data.network.ArtistMusicBrainzModel
import ly.david.data.network.EventMusicBrainzModel
import ly.david.data.network.InstrumentMusicBrainzModel
import ly.david.data.network.LabelMusicBrainzModel
import ly.david.data.network.PlaceMusicBrainzModel
import ly.david.data.network.RecordingMusicBrainzModel
import ly.david.data.network.ReleaseGroupMusicBrainzModel
import ly.david.data.network.ReleaseMusicBrainzModel
import ly.david.data.network.SeriesMusicBrainzModel
import ly.david.data.network.WorkMusicBrainzModel
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Search for MusicBrainz entities using text.
 */
interface SearchApi {

    suspend fun queryAreas(
        query: String,
        limit: Int = SEARCH_BROWSE_LIMIT,
        offset: Int = 0,
    ): SearchAreasResponse

    @GET("artist")
    suspend fun queryArtists(
        @Query("query") query: String,
        @Query("limit") limit: Int = SEARCH_BROWSE_LIMIT,
        @Query("offset") offset: Int = 0,
    ): SearchArtistsResponse

    @GET("event")
    suspend fun queryEvents(
        @Query("query") query: String,
        @Query("limit") limit: Int = SEARCH_BROWSE_LIMIT,
        @Query("offset") offset: Int = 0,
    ): SearchEventsResponse

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

    @GET("place")
    suspend fun queryPlaces(
        @Query("query") query: String,
        @Query("limit") limit: Int = SEARCH_BROWSE_LIMIT,
        @Query("offset") offset: Int = 0,
    ): SearchPlacesResponse

    @GET("recording")
    suspend fun queryRecordings(
        @Query("query") query: String,
        @Query("limit") limit: Int = SEARCH_BROWSE_LIMIT,
        @Query("offset") offset: Int = 0,
    ): SearchRecordingsResponse

    @GET("release")
    suspend fun queryReleases(
        @Query("query") query: String,
        @Query("limit") limit: Int = SEARCH_BROWSE_LIMIT,
        @Query("offset") offset: Int = 0,
    ): SearchReleasesResponse

    @GET("release-group")
    suspend fun queryReleaseGroups(
        @Query("query") query: String,
        @Query("limit") limit: Int = SEARCH_BROWSE_LIMIT,
        @Query("offset") offset: Int = 0,
    ): SearchReleaseGroupsResponse

    @GET("series")
    suspend fun querySeries(
        @Query("query") query: String,
        @Query("limit") limit: Int = SEARCH_BROWSE_LIMIT,
        @Query("offset") offset: Int = 0,
    ): SearchSeriesResponse

    @GET("work")
    suspend fun queryWorks(
        @Query("query") query: String,
        @Query("limit") limit: Int = SEARCH_BROWSE_LIMIT,
        @Query("offset") offset: Int = 0,
    ): SearchWorksResponse
}

interface SearchApiImpl : SearchApi {
    val client: HttpClient

    override suspend fun queryAreas(query: String, limit: Int, offset: Int): SearchAreasResponse {
        return client.get {
            url {
                appendPathSegments("area")
                parameter("query", query)
                parameter("limit", limit)
                parameter("offset", offset)
            }
        }.body()
    }

    override suspend fun queryArtists(query: String, limit: Int, offset: Int): SearchArtistsResponse {
        TODO("Not yet implemented")
    }

    override suspend fun queryEvents(query: String, limit: Int, offset: Int): SearchEventsResponse {
        TODO("Not yet implemented")
    }

    override suspend fun queryInstruments(query: String, limit: Int, offset: Int): SearchInstrumentsResponse {
        TODO("Not yet implemented")
    }

    override suspend fun queryLabels(query: String, limit: Int, offset: Int): SearchLabelsResponse {
        TODO("Not yet implemented")
    }

    override suspend fun queryPlaces(query: String, limit: Int, offset: Int): SearchPlacesResponse {
        TODO("Not yet implemented")
    }

    override suspend fun queryRecordings(query: String, limit: Int, offset: Int): SearchRecordingsResponse {
        TODO("Not yet implemented")
    }

    override suspend fun queryReleases(query: String, limit: Int, offset: Int): SearchReleasesResponse {
        TODO("Not yet implemented")
    }

    override suspend fun queryReleaseGroups(query: String, limit: Int, offset: Int): SearchReleaseGroupsResponse {
        TODO("Not yet implemented")
    }

    override suspend fun querySeries(query: String, limit: Int, offset: Int): SearchSeriesResponse {
        TODO("Not yet implemented")
    }

    override suspend fun queryWorks(query: String, limit: Int, offset: Int): SearchWorksResponse {
        TODO("Not yet implemented")
    }
}

@Serializable
data class SearchAreasResponse(
    @SerialName("count") val count: Int, // Total hits
    @SerialName("offset") val offset: Int,
    @SerialName("areas") val areas: List<AreaMusicBrainzModel>,
)

@Serializable
data class SearchArtistsResponse(
    @SerialName("count") val count: Int, // Total hits
    @SerialName("offset") val offset: Int,
    @SerialName("artists") val artists: List<ArtistMusicBrainzModel>,
)

@Serializable
data class SearchEventsResponse(
    @SerialName("count") val count: Int, // Total hits
    @SerialName("offset") val offset: Int,
    @SerialName("events") val events: List<EventMusicBrainzModel>,
)

@Serializable
data class SearchInstrumentsResponse(
    @SerialName("count") val count: Int, // Total hits
    @SerialName("offset") val offset: Int,
    @SerialName("instruments") val instruments: List<InstrumentMusicBrainzModel>,
)

@Serializable
data class SearchLabelsResponse(
    @SerialName("count") val count: Int, // Total hits
    @SerialName("offset") val offset: Int,
    @SerialName("labels") val labels: List<LabelMusicBrainzModel>,
)

@Serializable
data class SearchPlacesResponse(
    @SerialName("count") val count: Int, // Total hits
    @SerialName("offset") val offset: Int,
    @SerialName("places") val places: List<PlaceMusicBrainzModel>,
)

@Serializable
data class SearchRecordingsResponse(
    @SerialName("count") val count: Int, // Total hits
    @SerialName("offset") val offset: Int,
    @SerialName("recordings") val recordings: List<RecordingMusicBrainzModel>,
)

@Serializable
data class SearchReleasesResponse(
    @SerialName("count") val count: Int, // Total hits
    @SerialName("offset") val offset: Int,
    @SerialName("releases") val releases: List<ReleaseMusicBrainzModel>,
)

@Serializable
data class SearchReleaseGroupsResponse(
    @SerialName("count") val count: Int, // Total hits
    @SerialName("offset") val offset: Int,
    @SerialName("release-groups") val releaseGroups: List<ReleaseGroupMusicBrainzModel>,
)

@Serializable
data class SearchSeriesResponse(
    @SerialName("count") val count: Int, // Total hits
    @SerialName("offset") val offset: Int,
    @SerialName("series") val series: List<SeriesMusicBrainzModel>,
)

@Serializable
data class SearchWorksResponse(
    @SerialName("count") val count: Int, // Total hits
    @SerialName("offset") val offset: Int,
    @SerialName("works") val works: List<WorkMusicBrainzModel>,
)
