package ly.david.data.network

import ly.david.data.network.api.BrowseReleasesResponse
import ly.david.data.network.api.SearchReleasesResponse

val underPressureRemastered = ReleaseMusicBrainzModel(
    id = "eac6d0cd-1ed0-4e17-b5b0-d3cfc40547b2",
    name = "Under Pressure",
    artistCredits = listOf(queenArtistCredit, davidBowieArtistCredit),
    releaseGroup = underPressureReleaseGroup
)

val underPressureRemasterOf = RelationMusicBrainzModel(
    type = "remaster",
    typeId = "48e327b5-2d04-4518-93f1-fed5f0f0fa3c",
    direction = Direction.BACKWARD,
    targetType = MusicBrainzEntity.RELEASE,
    release = underPressureRemastered
)

val fakeReleaseEvent = ReleaseEvent(
    area = canada,
    date = "2022-10-29"
)

val underPressure = ReleaseMusicBrainzModel(
    id = "61735bf8-219e-3164-a94c-b74b1482fd01",
    name = "Under Pressure",
    date = "1981-10",
    artistCredits = listOf(queenArtistCredit, davidBowieArtistCredit),
    releaseGroup = underPressureReleaseGroup,
    releaseEvents = listOf(
        fakeReleaseEvent
    ),
    media = listOf(underPressureMedia),
    labelInfoList = listOf(
        underPressureLabelInfo,
        LabelInfo(
            label = elektraMusicGroup
        )
    ),
    relations = listOf(underPressureRemasterOf)
)

val fakeReleases = listOf(
    underPressure,
    underPressureRemastered
)

val browseReleasesResponse = BrowseReleasesResponse(
    count = 1,
    offset = 0,
    musicBrainzModels = listOf(underPressure)
)

val searchReleasesResponse = SearchReleasesResponse(
    count = 1,
    offset = 0,
    listOf(element = underPressure)
)
