package ly.david.musicsearch.data.musicbrainz.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.appendPathSegments
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ly.david.musicsearch.data.musicbrainz.SEARCH_BROWSE_LIMIT
import ly.david.musicsearch.data.musicbrainz.models.core.AreaMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.EventMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.InstrumentMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.LabelMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.PlaceMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.RecordingMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseGroupMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.SeriesMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.WorkMusicBrainzModel

/**
 * Search for MusicBrainz entities using text.
 */
interface SearchApi {

    suspend fun queryAreas(
        query: String,
        limit: Int = SEARCH_BROWSE_LIMIT,
        offset: Int = 0,
    ): SearchAreasResponse

    suspend fun queryArtists(
        query: String,
        limit: Int = SEARCH_BROWSE_LIMIT,
        offset: Int = 0,
    ): SearchArtistsResponse

    suspend fun queryEvents(
        query: String,
        limit: Int = SEARCH_BROWSE_LIMIT,
        offset: Int = 0,
    ): SearchEventsResponse

    suspend fun queryInstruments(
        query: String,
        limit: Int = SEARCH_BROWSE_LIMIT,
        offset: Int = 0,
    ): SearchInstrumentsResponse

    suspend fun queryLabels(
        query: String,
        limit: Int = SEARCH_BROWSE_LIMIT,
        offset: Int = 0,
    ): SearchLabelsResponse

    suspend fun queryPlaces(
        query: String,
        limit: Int = SEARCH_BROWSE_LIMIT,
        offset: Int = 0,
    ): SearchPlacesResponse

    suspend fun queryRecordings(
        query: String,
        limit: Int = SEARCH_BROWSE_LIMIT,
        offset: Int = 0,
    ): SearchRecordingsResponse

    suspend fun queryReleases(
        query: String,
        limit: Int = SEARCH_BROWSE_LIMIT,
        offset: Int = 0,
    ): SearchReleasesResponse

    suspend fun queryReleaseGroups(
        query: String,
        limit: Int = SEARCH_BROWSE_LIMIT,
        offset: Int = 0,
    ): SearchReleaseGroupsResponse

    suspend fun querySeries(
        query: String,
        limit: Int = SEARCH_BROWSE_LIMIT,
        offset: Int = 0,
    ): SearchSeriesResponse

    suspend fun queryWorks(
        query: String,
        limit: Int = SEARCH_BROWSE_LIMIT,
        offset: Int = 0,
    ): SearchWorksResponse
}

interface SearchApiImpl : SearchApi {
    val httpClient: HttpClient

    override suspend fun queryAreas(query: String, limit: Int, offset: Int): SearchAreasResponse {
        return httpClient.get {
            url {
                appendPathSegments("area")
                parameter("query", query)
                parameter("limit", limit)
                parameter("offset", offset)
            }
        }.body()
    }

    override suspend fun queryArtists(query: String, limit: Int, offset: Int): SearchArtistsResponse {
        return httpClient.get {
            url {
                appendPathSegments("artist")
                parameter("query", query)
                parameter("limit", limit)
                parameter("offset", offset)
            }
        }.body()
    }

    override suspend fun queryEvents(query: String, limit: Int, offset: Int): SearchEventsResponse {
        return httpClient.get {
            url {
                appendPathSegments("event")
                parameter("query", query)
                parameter("limit", limit)
                parameter("offset", offset)
            }
        }.body()
    }

    override suspend fun queryInstruments(query: String, limit: Int, offset: Int): SearchInstrumentsResponse {
        return httpClient.get {
            url {
                appendPathSegments("instrument")
                parameter("query", query)
                parameter("limit", limit)
                parameter("offset", offset)
            }
        }.body()
    }

    override suspend fun queryLabels(query: String, limit: Int, offset: Int): SearchLabelsResponse {
        return httpClient.get {
            url {
                appendPathSegments("label")
                parameter("query", query)
                parameter("limit", limit)
                parameter("offset", offset)
            }
        }.body()
    }

    override suspend fun queryPlaces(query: String, limit: Int, offset: Int): SearchPlacesResponse {
        return httpClient.get {
            url {
                appendPathSegments("place")
                parameter("query", query)
                parameter("limit", limit)
                parameter("offset", offset)
            }
        }.body()
    }

    override suspend fun queryRecordings(query: String, limit: Int, offset: Int): SearchRecordingsResponse {
        return httpClient.get {
            url {
                appendPathSegments("recording")
                parameter("query", query)
                parameter("limit", limit)
                parameter("offset", offset)
            }
        }.body()
    }

    override suspend fun queryReleases(query: String, limit: Int, offset: Int): SearchReleasesResponse {
        return httpClient.get {
            url {
                appendPathSegments("release")
                parameter("query", query)
                parameter("limit", limit)
                parameter("offset", offset)
            }
        }.body()
    }

    override suspend fun queryReleaseGroups(query: String, limit: Int, offset: Int): SearchReleaseGroupsResponse {
        return httpClient.get {
            url {
                appendPathSegments("release-group")
                parameter("query", query)
                parameter("limit", limit)
                parameter("offset", offset)
            }
        }.body()
    }

    override suspend fun querySeries(query: String, limit: Int, offset: Int): SearchSeriesResponse {
        return httpClient.get {
            url {
                appendPathSegments("series")
                parameter("query", query)
                parameter("limit", limit)
                parameter("offset", offset)
            }
        }.body()
    }

    override suspend fun queryWorks(query: String, limit: Int, offset: Int): SearchWorksResponse {
        return httpClient.get {
            url {
                appendPathSegments("work")
                parameter("query", query)
                parameter("limit", limit)
                parameter("offset", offset)
            }
        }.body()
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
