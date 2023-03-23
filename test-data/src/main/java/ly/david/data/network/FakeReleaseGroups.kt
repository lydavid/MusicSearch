package ly.david.data.network

import ly.david.data.network.api.BrowseReleaseGroupsResponse
import ly.david.data.network.api.SearchReleaseGroupsResponse

val fakeReleaseGroup = ReleaseGroupMusicBrainzModel(
    id = "fakeReleaseGroup1",
    name = "Release Group Name",
    artistCredits = listOf(fakeArtistCredit, fakeArtistCredit2),
    primaryType = "Album",
    relations = listOf(
        RelationMusicBrainzModel(
            type = "",
            typeId = "5e2907db-49ec-4a48-9f11-dfb99d2603ff",
            direction = Direction.BACKWARD,
            targetType = MusicBrainzResource.ARTIST,
            artist = fakeArtist
        )
    )
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

val fakeReleaseGroups = listOf(
    fakeReleaseGroup
)

val searchReleaseGroupsResponse = SearchReleaseGroupsResponse(
    count = 1,
    offset = 0,
    listOf(element = fakeReleaseGroup)
)
