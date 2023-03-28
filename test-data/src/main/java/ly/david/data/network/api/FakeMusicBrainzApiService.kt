package ly.david.data.network.api

import java.io.IOException
import ly.david.data.network.AreaMusicBrainzModel
import ly.david.data.network.ArtistMusicBrainzModel
import ly.david.data.network.EventMusicBrainzModel
import ly.david.data.network.GenreMusicBrainzModel
import ly.david.data.network.InstrumentMusicBrainzModel
import ly.david.data.network.LabelMusicBrainzModel
import ly.david.data.network.PlaceMusicBrainzModel
import ly.david.data.network.RecordingMusicBrainzModel
import ly.david.data.network.ReleaseGroupMusicBrainzModel
import ly.david.data.network.ReleaseMusicBrainzModel
import ly.david.data.network.SeriesMusicBrainzModel
import ly.david.data.network.WorkMusicBrainzModel
import ly.david.data.network.browseAreasResponse
import ly.david.data.network.browseArtistsResponse
import ly.david.data.network.browseCollectionsResponse
import ly.david.data.network.browseEventsResponse
import ly.david.data.network.browseInstrumentsResponse
import ly.david.data.network.browseLabelsResponse
import ly.david.data.network.browsePlacesResponse
import ly.david.data.network.browseRecordingsResponse
import ly.david.data.network.browseReleaseGroupsResponse
import ly.david.data.network.browseReleasesResponse
import ly.david.data.network.browseSeriesResponse
import ly.david.data.network.browseWorksResponse
import ly.david.data.network.fakeAreas
import ly.david.data.network.fakeArtists
import ly.david.data.network.fakeEvents
import ly.david.data.network.fakeGenres
import ly.david.data.network.fakeInstruments
import ly.david.data.network.fakeLabels
import ly.david.data.network.fakePlaces
import ly.david.data.network.fakeRecordings
import ly.david.data.network.fakeReleaseGroups
import ly.david.data.network.fakeReleases
import ly.david.data.network.fakeSeriesList
import ly.david.data.network.fakeWorks
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
    override suspend fun queryAreas(query: String, limit: Int, offset: Int): SearchAreasResponse {
        return searchAreasResponse
    }

    override suspend fun queryArtists(query: String, limit: Int, offset: Int): SearchArtistsResponse {
        return searchArtistsResponse
    }

    override suspend fun queryEvents(query: String, limit: Int, offset: Int): SearchEventsResponse {
        return searchEventsResponse
    }

    override suspend fun queryInstruments(query: String, limit: Int, offset: Int): SearchInstrumentsResponse {
        return searchInstrumentsResponse
    }

    override suspend fun queryLabels(query: String, limit: Int, offset: Int): SearchLabelsResponse {
        return searchLabelsResponse
    }

    override suspend fun queryPlaces(query: String, limit: Int, offset: Int): SearchPlacesResponse {
        return searchPlacesResponse
    }

    override suspend fun queryRecordings(query: String, limit: Int, offset: Int): SearchRecordingsResponse {
        return searchRecordingsResponse
    }

    override suspend fun queryReleases(query: String, limit: Int, offset: Int): SearchReleasesResponse {
        return searchReleasesResponse
    }

    override suspend fun queryReleaseGroups(query: String, limit: Int, offset: Int): SearchReleaseGroupsResponse {
        return searchReleaseGroupsResponse
    }

    override suspend fun querySeries(query: String, limit: Int, offset: Int): SearchSeriesResponse {
        return searchSeriesResponse
    }

    override suspend fun queryWorks(query: String, limit: Int, offset: Int): SearchWorksResponse {
        return searchWorksResponse
    }
    // endregion

    // region Browse
    override suspend fun browseAreasByCollection(collectionId: String, limit: Int, offset: Int): BrowseAreasResponse {
        return browseAreasResponse
    }

    override suspend fun browseArtistsByCollection(
        collectionId: String,
        limit: Int,
        offset: Int
    ): BrowseArtistsResponse {
        return browseArtistsResponse
    }

    override suspend fun browseCollectionsByUser(
        username: String,
        limit: Int,
        offset: Int,
        include: String?
    ): BrowseCollectionsResponse {
        return browseCollectionsResponse
    }

    override suspend fun browseEventsByCollection(collectionId: String, limit: Int, offset: Int): BrowseEventsResponse {
        return browseEventsResponse
    }

    override suspend fun browseEventsByPlace(placeId: String, limit: Int, offset: Int): BrowseEventsResponse {
        return browseEventsResponse
    }

    override suspend fun browseInstrumentsByCollection(
        collectionId: String,
        limit: Int,
        offset: Int
    ): BrowseInstrumentsResponse {
        return browseInstrumentsResponse
    }

    override suspend fun browseLabelsByCollection(collectionId: String, limit: Int, offset: Int): BrowseLabelsResponse {
        return browseLabelsResponse
    }

    override suspend fun browsePlacesByArea(areaId: String, limit: Int, offset: Int): BrowsePlacesResponse {
        return browsePlacesResponse
    }

    override suspend fun browsePlacesByCollection(collectionId: String, limit: Int, offset: Int): BrowsePlacesResponse {
        return browsePlacesResponse
    }

    override suspend fun browseRecordingsByCollection(
        collectionId: String,
        limit: Int,
        offset: Int
    ): BrowseRecordingsResponse {
        return browseRecordingsResponse
    }

    override suspend fun browseRecordingsByWork(workId: String, limit: Int, offset: Int): BrowseRecordingsResponse {
        return browseRecordingsResponse
    }

    override suspend fun browseReleasesByArea(
        areaId: String,
        limit: Int,
        offset: Int
    ): BrowseReleasesResponse {
        return browseReleasesResponse
    }

    override suspend fun browseReleasesByArtist(artistId: String, limit: Int, offset: Int): BrowseReleasesResponse {
        return browseReleasesResponse
    }

    override suspend fun browseReleasesByCollection(
        collectionId: String,
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

    override suspend fun browseReleasesByRecording(
        recordingId: String,
        limit: Int,
        offset: Int
    ): BrowseReleasesResponse {
        return browseReleasesResponse
    }

    override suspend fun browseReleasesByReleaseGroup(
        releaseGroupId: String,
        limit: Int,
        offset: Int
    ): BrowseReleasesResponse {
        return browseReleasesResponse
    }

    override suspend fun browseReleaseGroupsByArtist(
        artistId: String,
        limit: Int,
        offset: Int,
        include: String
    ): BrowseReleaseGroupsResponse {
        return browseReleaseGroupsResponse
    }

    override suspend fun browseReleaseGroupsByCollection(
        collectionId: String,
        limit: Int,
        offset: Int,
        include: String
    ): BrowseReleaseGroupsResponse {
        return browseReleaseGroupsResponse
    }

    override suspend fun browseSeriesByCollection(collectionId: String, limit: Int, offset: Int): BrowseSeriesResponse {
        return browseSeriesResponse
    }

    override suspend fun browseWorksByCollection(collectionId: String, limit: Int, offset: Int): BrowseWorksResponse {
        return browseWorksResponse
    }
    // endregion

    // region Lookup
    override suspend fun lookupArea(areaId: String, include: String?): AreaMusicBrainzModel {
        return fakeAreas.firstOrNull { it.id == areaId } ?: throw IOException()
    }

    override suspend fun lookupArtist(artistId: String, include: String?): ArtistMusicBrainzModel {
        return fakeArtists.firstOrNull { it.id == artistId } ?: throw IOException()
    }

    override suspend fun lookupEvent(eventId: String, include: String?): EventMusicBrainzModel {
        return fakeEvents.firstOrNull { it.id == eventId } ?: throw IOException()
    }

    override suspend fun lookupGenre(genreId: String, include: String?): GenreMusicBrainzModel {
        return fakeGenres.firstOrNull { it.id == genreId } ?: throw IOException()
    }

    override suspend fun lookupInstrument(instrumentId: String, include: String): InstrumentMusicBrainzModel {
        return fakeInstruments.firstOrNull { it.id == instrumentId } ?: throw IOException()
    }

    override suspend fun lookupLabel(labelId: String, include: String): LabelMusicBrainzModel {
        return fakeLabels.firstOrNull { it.id == labelId } ?: throw IOException()
    }

    override suspend fun lookupPlace(placeId: String, include: String?): PlaceMusicBrainzModel {
        return fakePlaces.firstOrNull { it.id == placeId } ?: throw IOException()
    }

    override suspend fun lookupRecording(recordingId: String, include: String): RecordingMusicBrainzModel {
        return fakeRecordings.firstOrNull { it.id == recordingId } ?: throw IOException()
    }

    override suspend fun lookupRelease(releaseId: String, include: String): ReleaseMusicBrainzModel {
        return fakeReleases.firstOrNull { it.id == releaseId } ?: throw IOException()
    }

    override suspend fun lookupReleaseGroup(releaseGroupId: String, include: String): ReleaseGroupMusicBrainzModel {
        return fakeReleaseGroups.firstOrNull { it.id == releaseGroupId } ?: throw IOException()
    }

    override suspend fun lookupSeries(seriesId: String, include: String?): SeriesMusicBrainzModel {
        return fakeSeriesList.firstOrNull { it.id == seriesId } ?: throw IOException()
    }

    override suspend fun lookupWork(workId: String, include: String?): WorkMusicBrainzModel {
        return fakeWorks.firstOrNull { it.id == workId } ?: throw IOException()
    }
    // endregion

    // region Collection
    override suspend fun uploadToCollection(
        collectionId: String,
        resourceUriPlural: String,
        mbids: String,
        client: String
    ) {
        TODO("Not yet implemented")
    }
    // endregion
}
