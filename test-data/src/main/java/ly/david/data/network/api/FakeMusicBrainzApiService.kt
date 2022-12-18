package ly.david.data.network.api

import ly.david.data.network.AreaMusicBrainzModel
import ly.david.data.network.ArtistMusicBrainzModel
import ly.david.data.network.EventMusicBrainzModel
import ly.david.data.network.InstrumentMusicBrainzModel
import ly.david.data.network.LabelMusicBrainzModel
import ly.david.data.network.PlaceMusicBrainzModel
import ly.david.data.network.RecordingMusicBrainzModel
import ly.david.data.network.ReleaseGroupMusicBrainzModel
import ly.david.data.network.ReleaseMusicBrainzModel
import ly.david.data.network.WorkMusicBrainzModel
import ly.david.data.network.browseEventsResponse
import ly.david.data.network.browsePlacesResponse
import ly.david.data.network.browseRecordingsResponse
import ly.david.data.network.browseReleaseGroupsResponse
import ly.david.data.network.browseReleasesResponse
import ly.david.data.network.fakeArea
import ly.david.data.network.fakeAreas
import ly.david.data.network.fakeArtist
import ly.david.data.network.fakeEvent
import ly.david.data.network.fakeLabel
import ly.david.data.network.fakePlace
import ly.david.data.network.fakePlaces
import ly.david.data.network.fakeRecording
import ly.david.data.network.fakeRelease
import ly.david.data.network.fakeReleaseGroup
import ly.david.data.network.fakeReleases
import ly.david.data.network.fakeWorkWithAllData
import ly.david.data.network.instrumentMusicBrainzModel
import ly.david.data.network.searchAreasResponse
import ly.david.data.network.searchArtistsResponse
import ly.david.data.network.searchEventsResponse
import ly.david.data.network.searchInstrumentsResponse
import ly.david.data.network.searchLabelsResponse
import ly.david.data.network.searchPlacesResponse
import ly.david.data.network.searchRecordingsResponse
import ly.david.data.network.searchReleaseGroupsResponse
import ly.david.data.network.searchReleasesResponse
import ly.david.data.network.searchSeriesResponse
import ly.david.data.network.searchWorksResponse

class FakeMusicBrainzApiService : MusicBrainzApiService {
    // region Search
    override suspend fun queryArtists(query: String, limit: Int, offset: Int): SearchArtistsResponse {
        return searchArtistsResponse
    }

    override suspend fun queryReleaseGroups(query: String, limit: Int, offset: Int): SearchReleaseGroupsResponse {
        return searchReleaseGroupsResponse
    }

    override suspend fun queryReleases(query: String, limit: Int, offset: Int): SearchReleasesResponse {
        return searchReleasesResponse
    }

    override suspend fun queryRecordings(query: String, limit: Int, offset: Int): SearchRecordingsResponse {
        return searchRecordingsResponse
    }

    override suspend fun queryWorks(query: String, limit: Int, offset: Int): SearchWorksResponse {
        return searchWorksResponse
    }

    override suspend fun queryAreas(query: String, limit: Int, offset: Int): SearchAreasResponse {
        return searchAreasResponse
    }

    override suspend fun queryPlaces(query: String, limit: Int, offset: Int): SearchPlacesResponse {
        return searchPlacesResponse
    }

    override suspend fun queryInstruments(query: String, limit: Int, offset: Int): SearchInstrumentsResponse {
        return searchInstrumentsResponse
    }

    override suspend fun queryLabels(query: String, limit: Int, offset: Int): SearchLabelsResponse {
        return searchLabelsResponse
    }

    override suspend fun queryEvents(query: String, limit: Int, offset: Int): SearchEventsResponse {
        return searchEventsResponse
    }

    override suspend fun querySeries(query: String, limit: Int, offset: Int): SearchSeriesResponse {
        return searchSeriesResponse
    }

    // endregion

    // region Browse
    override suspend fun browseEventsByPlace(placeId: String, limit: Int, offset: Int): BrowseEventsResponse {
        return browseEventsResponse
    }

    override suspend fun browsePlacesByArea(areaId: String, limit: Int, offset: Int): BrowsePlacesResponse {
        return browsePlacesResponse
    }

    override suspend fun browseRecordingsByWork(workId: String, limit: Int, offset: Int): BrowseRecordingsResponse {
        return browseRecordingsResponse
    }

    override suspend fun browseReleaseGroupsByArtist(
        artistId: String,
        limit: Int,
        offset: Int,
        include: String
    ): BrowseReleaseGroupsResponse {
        return browseReleaseGroupsResponse
    }

    override suspend fun browseReleasesByReleaseGroup(
        releaseGroupId: String,
        limit: Int,
        offset: Int
    ): BrowseReleasesResponse {
        return browseReleasesResponse
    }

    override suspend fun browseReleasesByLabel(
        labelId: String,
        limit: Int,
        offset: Int,
        include: String
    ): BrowseReleasesResponse {
        return browseReleasesResponse
    }

    override suspend fun browseReleasesByArea(
        areaId: String,
        limit: Int,
        offset: Int
    ): BrowseReleasesResponse {
        return browseReleasesResponse
    }

    override suspend fun browseReleasesByRecording(
        recordingId: String,
        limit: Int,
        offset: Int
    ): BrowseReleasesResponse {
        return browseReleasesResponse
    }
    // endregion

    // region Lookup
    override suspend fun lookupArea(areaId: String, include: String): AreaMusicBrainzModel {
        return fakeAreas.firstOrNull { it.id == areaId } ?: fakeArea
    }

    override suspend fun lookupArtist(artistId: String, include: String?): ArtistMusicBrainzModel {
        return fakeArtist
    }

    override suspend fun lookupEvent(eventId: String, include: String?): EventMusicBrainzModel {
        return fakeEvent
    }

    override suspend fun lookupInstrument(instrumentId: String, include: String): InstrumentMusicBrainzModel {
        return instrumentMusicBrainzModel
    }

    override suspend fun lookupLabel(labelId: String, include: String): LabelMusicBrainzModel {
        return fakeLabel
    }

    override suspend fun lookupPlace(placeId: String, include: String?): PlaceMusicBrainzModel {
        return fakePlaces.firstOrNull { it.id == placeId } ?: fakePlace
    }

    override suspend fun lookupRecording(recordingId: String, include: String): RecordingMusicBrainzModel {
        return fakeRecording
    }

    override suspend fun lookupRelease(releaseId: String, include: String): ReleaseMusicBrainzModel {
        return fakeReleases.firstOrNull { it.id == releaseId } ?: fakeRelease
    }

    override suspend fun lookupReleaseGroup(releaseGroupId: String, include: String): ReleaseGroupMusicBrainzModel {
        return fakeReleaseGroup
    }

    override suspend fun lookupWork(workId: String, include: String?): WorkMusicBrainzModel {
        return fakeWorkWithAllData
    }
    // endregion
}
