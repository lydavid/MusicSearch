package ly.david.data.test.api

import ly.david.data.common.network.RecoverableNetworkException
import ly.david.musicsearch.data.musicbrainz.AreaMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.ArtistMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.EventMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.GenreMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.InstrumentMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.LabelMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.PlaceMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.RecordingMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.ReleaseGroupMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.ReleaseMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.SeriesMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.WorkMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.api.BrowseAreasResponse
import ly.david.musicsearch.data.musicbrainz.api.BrowseArtistsResponse
import ly.david.musicsearch.data.musicbrainz.api.BrowseCollectionsResponse
import ly.david.musicsearch.data.musicbrainz.api.BrowseEventsResponse
import ly.david.musicsearch.data.musicbrainz.api.BrowseInstrumentsResponse
import ly.david.musicsearch.data.musicbrainz.api.BrowseLabelsResponse
import ly.david.musicsearch.data.musicbrainz.api.BrowsePlacesResponse
import ly.david.musicsearch.data.musicbrainz.api.BrowseRecordingsResponse
import ly.david.musicsearch.data.musicbrainz.api.BrowseReleaseGroupsResponse
import ly.david.musicsearch.data.musicbrainz.api.BrowseReleasesResponse
import ly.david.musicsearch.data.musicbrainz.api.BrowseSeriesResponse
import ly.david.musicsearch.data.musicbrainz.api.BrowseWorksResponse
import ly.david.musicsearch.data.musicbrainz.api.MusicBrainzApi
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
import ly.david.musicsearch.data.musicbrainz.api.UserInfo
import ly.david.data.test.browseAreasResponse
import ly.david.data.test.browseArtistsResponse
import ly.david.data.test.browseCollectionsResponse
import ly.david.data.test.browseEventsResponse
import ly.david.data.test.browseInstrumentsResponse
import ly.david.data.test.browseLabelsResponse
import ly.david.data.test.browsePlacesResponse
import ly.david.data.test.browseRecordingsResponse
import ly.david.data.test.browseReleaseGroupsResponse
import ly.david.data.test.browseReleasesResponse
import ly.david.data.test.browseSeriesResponse
import ly.david.data.test.browseWorksResponse
import ly.david.data.test.fakeAreas
import ly.david.data.test.fakeArtists
import ly.david.data.test.fakeEvents
import ly.david.data.test.fakeGenres
import ly.david.data.test.fakeInstruments
import ly.david.data.test.fakeLabels
import ly.david.data.test.fakePlaces
import ly.david.data.test.fakeRecordings
import ly.david.data.test.fakeReleaseGroups
import ly.david.data.test.fakeReleases
import ly.david.data.test.fakeSeriesList
import ly.david.data.test.fakeWorks
import ly.david.data.test.searchAreasResponse
import ly.david.data.test.searchArtistsResponse
import ly.david.data.test.searchEventsResponse
import ly.david.data.test.searchInstrumentsResponse
import ly.david.data.test.searchLabelsResponse
import ly.david.data.test.searchPlacesResponse
import ly.david.data.test.searchRecordingsResponse
import ly.david.data.test.searchReleaseGroupsResponse
import ly.david.data.test.searchReleasesResponse
import ly.david.data.test.searchSeriesResponse
import ly.david.data.test.searchWorksResponse

class FakeMusicBrainzApi : MusicBrainzApi {
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
    override suspend fun browseAreasByCollection(
        collectionId: String,
        limit: Int,
        offset: Int,
    ): BrowseAreasResponse {
        return browseAreasResponse
    }

    override suspend fun browseArtistsByCollection(
        collectionId: String,
        limit: Int,
        offset: Int,
    ): BrowseArtistsResponse {
        return browseArtistsResponse
    }

    override suspend fun browseCollectionsByUser(
        username: String,
        limit: Int,
        offset: Int,
        include: String?,
    ): BrowseCollectionsResponse {
        return browseCollectionsResponse
    }

    override suspend fun browseEventsByCollection(
        collectionId: String,
        limit: Int,
        offset: Int,
    ): BrowseEventsResponse {
        return browseEventsResponse
    }

    override suspend fun browseEventsByPlace(placeId: String, limit: Int, offset: Int): BrowseEventsResponse {
        return browseEventsResponse
    }

    override suspend fun browseInstrumentsByCollection(
        collectionId: String,
        limit: Int,
        offset: Int,
    ): BrowseInstrumentsResponse {
        return browseInstrumentsResponse
    }

    override suspend fun browseLabelsByCollection(
        collectionId: String,
        limit: Int,
        offset: Int,
    ): BrowseLabelsResponse {
        return browseLabelsResponse
    }

    override suspend fun browsePlacesByArea(areaId: String, limit: Int, offset: Int): BrowsePlacesResponse {
        return browsePlacesResponse
    }

    override suspend fun browsePlacesByCollection(
        collectionId: String,
        limit: Int,
        offset: Int,
    ): BrowsePlacesResponse {
        return browsePlacesResponse
    }

    override suspend fun browseRecordingsByCollection(
        collectionId: String,
        limit: Int,
        offset: Int,
        include: String,
    ): BrowseRecordingsResponse {
        return browseRecordingsResponse
    }

    override suspend fun browseRecordingsByWork(
        workId: String,
        limit: Int,
        offset: Int,
        include: String,
    ): BrowseRecordingsResponse {
        return browseRecordingsResponse
    }

    override suspend fun browseReleasesByArea(
        areaId: String,
        limit: Int,
        offset: Int,
        include: String,
    ): BrowseReleasesResponse {
        return browseReleasesResponse
    }

    override suspend fun browseReleasesByArtist(
        artistId: String,
        limit: Int,
        offset: Int,
        include: String,
    ): BrowseReleasesResponse {
        return browseReleasesResponse
    }

    override suspend fun browseReleasesByCollection(
        collectionId: String,
        limit: Int,
        offset: Int,
        include: String,
    ): BrowseReleasesResponse {
        return browseReleasesResponse
    }

    override suspend fun browseReleasesByLabel(
        labelId: String,
        limit: Int,
        offset: Int,
        include: String,
    ): BrowseReleasesResponse {
        return browseReleasesResponse
    }

    override suspend fun browseReleasesByRecording(
        recordingId: String,
        limit: Int,
        offset: Int,
        include: String,
    ): BrowseReleasesResponse {
        return browseReleasesResponse
    }

    override suspend fun browseReleasesByReleaseGroup(
        releaseGroupId: String,
        limit: Int,
        offset: Int,
        include: String,
    ): BrowseReleasesResponse {
        return browseReleasesResponse
    }

    override suspend fun browseReleaseGroupsByArtist(
        artistId: String,
        limit: Int,
        offset: Int,
        include: String,
    ): BrowseReleaseGroupsResponse {
        return browseReleaseGroupsResponse
    }

    override suspend fun browseReleaseGroupsByCollection(
        collectionId: String,
        limit: Int,
        offset: Int,
        include: String,
    ): BrowseReleaseGroupsResponse {
        return browseReleaseGroupsResponse
    }

    override suspend fun browseSeriesByCollection(
        collectionId: String,
        limit: Int,
        offset: Int,
    ): BrowseSeriesResponse {
        return browseSeriesResponse
    }

    override suspend fun browseWorksByCollection(
        collectionId: String,
        limit: Int,
        offset: Int,
    ): BrowseWorksResponse {
        return browseWorksResponse
    }
    // endregion

    // region Lookup
    override suspend fun lookupArea(areaId: String, include: String?): AreaMusicBrainzModel {
        return fakeAreas.firstOrNull { it.id == areaId } ?: throw RecoverableNetworkException(message = "")
    }

    override suspend fun lookupArtist(artistId: String, include: String?): ArtistMusicBrainzModel {
        return fakeArtists.firstOrNull { it.id == artistId } ?: throw RecoverableNetworkException(message = "")
    }

    override suspend fun lookupEvent(eventId: String, include: String?): EventMusicBrainzModel {
        return fakeEvents.firstOrNull { it.id == eventId } ?: throw RecoverableNetworkException(message = "")
    }

    override suspend fun lookupGenre(genreId: String, include: String?): GenreMusicBrainzModel {
        return fakeGenres.firstOrNull { it.id == genreId } ?: throw RecoverableNetworkException(message = "")
    }

    override suspend fun lookupInstrument(instrumentId: String, include: String): InstrumentMusicBrainzModel {
        return fakeInstruments.firstOrNull { it.id == instrumentId } ?: throw RecoverableNetworkException(message = "")
    }

    override suspend fun lookupLabel(labelId: String, include: String): LabelMusicBrainzModel {
        return fakeLabels.firstOrNull { it.id == labelId } ?: throw RecoverableNetworkException(message = "")
    }

    override suspend fun lookupPlace(placeId: String, include: String?): PlaceMusicBrainzModel {
        return fakePlaces.firstOrNull { it.id == placeId } ?: throw RecoverableNetworkException(message = "")
    }

    override suspend fun lookupRecording(recordingId: String, include: String): RecordingMusicBrainzModel {
        return fakeRecordings.firstOrNull { it.id == recordingId } ?: throw RecoverableNetworkException(message = "")
    }

    override suspend fun lookupRelease(releaseId: String, include: String): ReleaseMusicBrainzModel {
        return fakeReleases.firstOrNull { it.id == releaseId } ?: throw RecoverableNetworkException(message = "")
    }

    override suspend fun lookupReleaseGroup(releaseGroupId: String, include: String): ReleaseGroupMusicBrainzModel {
        return fakeReleaseGroups.firstOrNull { it.id == releaseGroupId }
            ?: throw RecoverableNetworkException(message = "")
    }

    override suspend fun lookupSeries(seriesId: String, include: String?): SeriesMusicBrainzModel {
        return fakeSeriesList.firstOrNull { it.id == seriesId } ?: throw RecoverableNetworkException(message = "")
    }

    override suspend fun lookupWork(workId: String, include: String?): WorkMusicBrainzModel {
        return fakeWorks.firstOrNull { it.id == workId } ?: throw RecoverableNetworkException(message = "")
    }
    // endregion

    // region Collection
    override suspend fun uploadToCollection(
        collectionId: String,
        resourceUriPlural: String,
        mbids: String,
        client: String,
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFromCollection(
        collectionId: String,
        resourceUriPlural: String,
        mbids: String,
        client: String,
    ) {
        TODO("Not yet implemented")
    }
    // endregion

    // region User
    override suspend fun getUserInfo(): UserInfo {
        TODO("Not yet implemented")
    }

    override suspend fun logout(token: String, clientId: String, clientSecret: String) {
        TODO("Not yet implemented")
    }
    // endregion
}
