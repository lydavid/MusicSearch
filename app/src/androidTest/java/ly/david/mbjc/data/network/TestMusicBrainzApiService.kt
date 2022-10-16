package ly.david.mbjc.data.network

// If instrumentation tests just says 0 pass without showing any tests ran/failed,
//  then chances are we forgot to implement something here. It just doesn't tell us...
internal class TestMusicBrainzApiService : MusicBrainzApiService {
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
        TODO("Not yet implemented")
    }

    override suspend fun browseReleasesByReleaseGroup(
        releaseGroupId: String,
        limit: Int,
        offset: Int
    ): BrowseReleasesResponse {
        TODO("Not yet implemented")
    }

    override suspend fun browseReleasesByLabel(labelId: String, limit: Int, offset: Int): BrowseReleasesResponse {
        TODO("Not yet implemented")
    }

    override suspend fun browseReleasesByArea(areaId: String, limit: Int, offset: Int): BrowseReleasesResponse {
        TODO("Not yet implemented")
    }

    override suspend fun browseRecordingsByRelease(
        releaseId: String,
        limit: Int,
        offset: Int
    ): BrowseRecordingsResponse {
        TODO("Not yet implemented")
    }

    override suspend fun lookupArtist(artistId: String, include: String?): ArtistMusicBrainzModel {
        TODO("Not yet implemented")
    }

    override suspend fun lookupReleaseGroup(releaseGroupId: String, include: String): ReleaseGroupMusicBrainzModel {
        TODO("Not yet implemented")
    }

    override suspend fun lookupRelease(releaseId: String, include: String): ReleaseMusicBrainzModel {
        TODO("Not yet implemented")
    }

    override suspend fun lookupRecording(recordingId: String, include: String): RecordingMusicBrainzModel {
        TODO("Not yet implemented")
    }

    override suspend fun lookupWork(workId: String, include: String): WorkMusicBrainzModel {
        TODO("Not yet implemented")
    }

    override suspend fun lookupArea(areaId: String, include: String): AreaMusicBrainzModel {
        TODO("Not yet implemented")
    }

    override suspend fun lookupPlace(placeId: String, include: String): PlaceMusicBrainzModel {
        TODO("Not yet implemented")
    }

    override suspend fun lookupInstrument(instrumentId: String, include: String): InstrumentMusicBrainzModel {
        TODO("Not yet implemented")
    }

    override suspend fun lookupLabel(labelId: String, include: String): LabelMusicBrainzModel {
        TODO("Not yet implemented")
    }

    override suspend fun lookupEvent(eventId: String, include: String): EventMusicBrainzModel {
        TODO("Not yet implemented")
    }
}
