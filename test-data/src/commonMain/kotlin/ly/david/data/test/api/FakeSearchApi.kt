package ly.david.data.test.api

import ly.david.musicsearch.data.musicbrainz.api.SearchApi
import ly.david.musicsearch.data.musicbrainz.api.SearchAreasResponse
import ly.david.musicsearch.data.musicbrainz.api.SearchArtistsResponse
import ly.david.musicsearch.data.musicbrainz.api.SearchEventsResponse
import ly.david.musicsearch.data.musicbrainz.api.SearchInstrumentsResponse
import ly.david.musicsearch.data.musicbrainz.api.SearchLabelsResponse
import ly.david.musicsearch.data.musicbrainz.api.SearchPlacesResponse
import ly.david.musicsearch.data.musicbrainz.api.SearchRecordingsResponse
import ly.david.musicsearch.data.musicbrainz.api.SearchReleaseGroupsResponse
import ly.david.musicsearch.data.musicbrainz.api.SearchReleasesResponse
import ly.david.musicsearch.data.musicbrainz.api.SearchSeriesResponse
import ly.david.musicsearch.data.musicbrainz.api.SearchWorksResponse

open class FakeSearchApi : SearchApi {
    override suspend fun queryAreas(
        query: String,
        limit: Int,
        offset: Int,
    ): SearchAreasResponse {
        return SearchAreasResponse()
    }

    override suspend fun queryArtists(
        query: String,
        limit: Int,
        offset: Int,
    ): SearchArtistsResponse {
        return SearchArtistsResponse()
    }

    override suspend fun queryEvents(
        query: String,
        limit: Int,
        offset: Int,
    ): SearchEventsResponse {
        return SearchEventsResponse()
    }

    override suspend fun queryInstruments(
        query: String,
        limit: Int,
        offset: Int,
    ): SearchInstrumentsResponse {
        return SearchInstrumentsResponse()
    }

    override suspend fun queryLabels(
        query: String,
        limit: Int,
        offset: Int,
    ): SearchLabelsResponse {
        return SearchLabelsResponse()
    }

    override suspend fun queryPlaces(
        query: String,
        limit: Int,
        offset: Int,
    ): SearchPlacesResponse {
        return SearchPlacesResponse()
    }

    override suspend fun queryRecordings(
        query: String,
        limit: Int,
        offset: Int,
    ): SearchRecordingsResponse {
        return SearchRecordingsResponse()
    }

    override suspend fun queryReleases(
        query: String,
        limit: Int,
        offset: Int,
    ): SearchReleasesResponse {
        return SearchReleasesResponse()
    }

    override suspend fun queryReleaseGroups(
        query: String,
        limit: Int,
        offset: Int,
    ): SearchReleaseGroupsResponse {
        return SearchReleaseGroupsResponse()
    }

    override suspend fun querySeries(
        query: String,
        limit: Int,
        offset: Int,
    ): SearchSeriesResponse {
        return SearchSeriesResponse()
    }

    override suspend fun queryWorks(
        query: String,
        limit: Int,
        offset: Int,
    ): SearchWorksResponse {
        return SearchWorksResponse()
    }
}
