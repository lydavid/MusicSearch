package ly.david.mbjc.data.network.api

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
import ly.david.data.network.api.BrowseRecordingsResponse
import ly.david.data.network.api.BrowseReleaseGroupsResponse
import ly.david.data.network.api.BrowseReleasesResponse
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.network.api.SearchAreasResponse
import ly.david.data.network.api.SearchArtistsResponse
import ly.david.data.network.api.SearchEventsResponse
import ly.david.data.network.api.SearchInstrumentsResponse
import ly.david.data.network.api.SearchLabelsResponse
import ly.david.data.network.api.SearchPlacesResponse
import ly.david.data.network.api.SearchRecordingsResponse
import ly.david.data.network.api.SearchReleaseGroupsResponse
import ly.david.data.network.api.SearchReleasesResponse
import ly.david.data.network.api.SearchSeriesResponse
import ly.david.data.network.api.SearchWorksResponse
import ly.david.mbjc.data.network.areaMusicBrainzModel
import ly.david.mbjc.data.network.artistMusicBrainzModel
import ly.david.mbjc.data.network.browseRecordingsResponse
import ly.david.mbjc.data.network.browseReleaseGroupsResponse
import ly.david.mbjc.data.network.browseReleasesResponse
import ly.david.mbjc.data.network.eventMusicBrainzModel
import ly.david.mbjc.data.network.instrumentMusicBrainzModel
import ly.david.mbjc.data.network.labelMusicBrainzResource
import ly.david.mbjc.data.network.placeMusicBrainzModel
import ly.david.mbjc.data.network.recordingMusicBrainzModel
import ly.david.mbjc.data.network.releaseGroupMusicBrainzModel
import ly.david.mbjc.data.network.releaseMusicBrainzModel
import ly.david.mbjc.data.network.workGroupMusicBrainzModel

internal class FakeMusicBrainzApiService : MusicBrainzApiService {
    override suspend fun queryArtists(query: String, limit: Int, offset: Int): SearchArtistsResponse {
        return SearchArtistsResponse(
            1,
            0,
            listOf(
                ArtistMusicBrainzModel(
                    id = "1",
                    name = "artist name",
                    sortName = "sort name"
                )
            )
        )
    }

    override suspend fun queryReleaseGroups(query: String, limit: Int, offset: Int): SearchReleaseGroupsResponse {
        return SearchReleaseGroupsResponse(
            1,
            0,
            listOf(
                ReleaseGroupMusicBrainzModel(
                    id = "1",
                    name = "release group name",
                    firstReleaseDate = "2022-03-14"
                )
            )
        )
    }

    override suspend fun queryReleases(query: String, limit: Int, offset: Int): SearchReleasesResponse {
        TODO("Not yet implemented")
    }

    override suspend fun queryRecordings(query: String, limit: Int, offset: Int): SearchRecordingsResponse {
        TODO("Not yet implemented")
    }

    override suspend fun queryWorks(query: String, limit: Int, offset: Int): SearchWorksResponse {
        TODO("Not yet implemented")
    }

    override suspend fun queryAreas(query: String, limit: Int, offset: Int): SearchAreasResponse {
        TODO("Not yet implemented")
    }

    override suspend fun queryPlaces(query: String, limit: Int, offset: Int): SearchPlacesResponse {
        TODO("Not yet implemented")
    }

    override suspend fun queryInstruments(query: String, limit: Int, offset: Int): SearchInstrumentsResponse {
        TODO("Not yet implemented")
    }

    override suspend fun queryLabels(query: String, limit: Int, offset: Int): SearchLabelsResponse {
        TODO("Not yet implemented")
    }

    override suspend fun queryEvents(query: String, limit: Int, offset: Int): SearchEventsResponse {
        TODO("Not yet implemented")
    }

    override suspend fun querySeries(query: String, limit: Int, offset: Int): SearchSeriesResponse {
        TODO("Not yet implemented")
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

    override suspend fun browseReleasesByLabel(labelId: String, limit: Int, offset: Int): BrowseReleasesResponse {
        return browseReleasesResponse
    }

    override suspend fun browseReleasesByArea(areaId: String, limit: Int, offset: Int): BrowseReleasesResponse {
        return browseReleasesResponse
    }

    override suspend fun browseRecordingsByRelease(
        releaseId: String,
        limit: Int,
        offset: Int
    ): BrowseRecordingsResponse {
        return browseRecordingsResponse
    }

    override suspend fun lookupArea(areaId: String, include: String): AreaMusicBrainzModel {
        return areaMusicBrainzModel
    }

    override suspend fun lookupArtist(artistId: String, include: String?): ArtistMusicBrainzModel {
        return artistMusicBrainzModel
    }

    override suspend fun lookupEvent(eventId: String, include: String): EventMusicBrainzModel {
        return eventMusicBrainzModel
    }

    override suspend fun lookupInstrument(instrumentId: String, include: String): InstrumentMusicBrainzModel {
        return instrumentMusicBrainzModel
    }

    override suspend fun lookupLabel(labelId: String, include: String): LabelMusicBrainzModel {
        return labelMusicBrainzResource
    }

    override suspend fun lookupPlace(placeId: String, include: String): PlaceMusicBrainzModel {
        return placeMusicBrainzModel
    }

    override suspend fun lookupRecording(recordingId: String, include: String): RecordingMusicBrainzModel {
        return recordingMusicBrainzModel
    }

    override suspend fun lookupRelease(releaseId: String, include: String): ReleaseMusicBrainzModel {
        return releaseMusicBrainzModel
    }

    override suspend fun lookupReleaseGroup(releaseGroupId: String, include: String): ReleaseGroupMusicBrainzModel {
        return releaseGroupMusicBrainzModel
    }

    override suspend fun lookupWork(workId: String, include: String): WorkMusicBrainzModel {
        return workGroupMusicBrainzModel
    }
}
