package ly.david.mbjc.data.network

internal val artistMusicBrainzModel = ArtistMusicBrainzModel(
    id = "1",
    name = "Artist Name",
)

internal val releaseGroupMusicBrainzModel = ReleaseGroupMusicBrainzModel(
    id = "1",
    name = "Release Group Name",
)

internal val browseReleaseGroupsResponse = BrowseReleaseGroupsResponse(
    count = 1,
    offset = 0,
    releaseGroups = listOf(releaseGroupMusicBrainzModel)
)
