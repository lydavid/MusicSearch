package ly.david.data.network

import ly.david.data.network.api.BrowseReleasesResponse
import ly.david.data.network.api.SearchReleasesResponse

val fakeRelease2 = ReleaseMusicBrainzModel(
    id = "fakeRelease2",
    name = "Fake Release Relationship",
    artistCredits = listOf(fakeArtistCredit, fakeArtistCredit2),
    releaseGroup = fakeReleaseGroup
)

val remasterOf = RelationMusicBrainzModel(
    type = "blah",
    typeId = "48e327b5-2d04-4518-93f1-fed5f0f0fa3c",
    direction = Direction.FORWARD,
    targetType = MusicBrainzResource.RELEASE,
    release = fakeRelease2
)

val fakeReleaseEvent = ReleaseEvent(
    area = fakeCountry,
    date = "2022-10-29"
)

val fakeRelease = ReleaseMusicBrainzModel(
    id = "fakeRelease1",
    name = "Release Name",
    date = "2023-03-22",
    artistCredits = listOf(fakeArtistCredit, fakeArtistCredit2),
    releaseGroup = fakeReleaseGroup,
    releaseEvents = listOf(
        fakeReleaseEvent
    ),
    media = listOf(fakeMedia),
    labelInfoList = listOf(
        fakeLabelInfo,
        LabelInfo(
            label = fakeLabel2
        )
    ),
    relations = listOf(remasterOf)
)

val fakeReleases = listOf(
    fakeRelease,
    fakeRelease2
)

val browseReleasesResponse = BrowseReleasesResponse(
    count = 1,
    offset = 0,
    musicBrainzModels = listOf(fakeRelease)
)

val searchReleasesResponse = SearchReleasesResponse(
    count = 1,
    offset = 0,
    listOf(element = fakeRelease)
)
