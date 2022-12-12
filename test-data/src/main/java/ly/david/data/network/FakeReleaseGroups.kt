package ly.david.data.network

import ly.david.data.network.api.BrowseReleaseGroupsResponse

val fakeReleaseGroup = ReleaseGroupMusicBrainzModel(
    id = "fakeReleaseGroup1",
    name = "Release Group Name",
)

val fakeReleaseGroupWithArtistCredits = ReleaseGroupMusicBrainzModel(
    id = "fakeReleaseGroup2",
    name = "Release Group With Artist Credits",
    artistCredits = listOf(fakeArtistCredit, fakeArtistCredit2)
)

val browseReleaseGroupsResponse = BrowseReleaseGroupsResponse(
    count = 1,
    offset = 0,
    releaseGroups = listOf(fakeReleaseGroup)
)
