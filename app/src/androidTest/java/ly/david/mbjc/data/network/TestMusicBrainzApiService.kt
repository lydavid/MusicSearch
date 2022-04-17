package ly.david.mbjc.data.network

class TestMusicBrainzApiService : MusicBrainzApiService {
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

    override suspend fun browseRecordingsByRelease(
        releaseId: String,
        limit: Int,
        offset: Int
    ): BrowseRecordingsResponse {
        TODO("Not yet implemented")
    }

    override suspend fun lookupArtist(artistId: String): ArtistMusicBrainzModel {
        TODO("Not yet implemented")
    }

    override suspend fun lookupReleaseGroup(releaseGroupId: String, include: String): ReleaseGroupMusicBrainzModel {
        TODO("Not yet implemented")
    }

    override suspend fun lookupRelease(releaseId: String, include: String): ReleaseMusicBrainzModel {
        TODO("Not yet implemented")
    }
}
