package ly.david.data.network

val fakeReleaseGroup = ReleaseGroupMusicBrainzModel(
    id = "fakeReleaseGroup1",
    name = "Release Group Name",
)

val fakeReleaseGroupWithArtistCredits = ReleaseGroupMusicBrainzModel(
    id = "fakeReleaseGroup2",
    name = "Release Group With Artist Credits",
    artistCredits = listOf(fakeArtistCredit, fakeArtistCredit2)
)
