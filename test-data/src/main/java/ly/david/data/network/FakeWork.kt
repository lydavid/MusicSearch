package ly.david.data.network

import ly.david.data.network.api.BrowseWorksResponse
import ly.david.data.network.api.SearchWorksResponse

val fakeWorkAttribute = WorkAttributeMusicBrainzModel(
    type = "SUISA ID",
    typeId = "034f35ae-d250-3749-95e7-854e606d5960",
    value = "000321 768 01"
)

val fakeWork2 = WorkMusicBrainzModel(
    id = "w2",
    name = "work 2",
)

val arrangements = RelationMusicBrainzModel(
    type = "doesn't matter",
    typeId = "51975ed8-bbfa-486b-9f28-5947f4370299",
    direction = Direction.FORWARD,
    targetType = MusicBrainzEntity.WORK,
    work = fakeWork2
)

val fakeWorkWithAllData = WorkMusicBrainzModel(
    id = "w1",
    name = "Work Name",
    type = "Song",
    attributes = listOf(
        fakeWorkAttribute
    ),
    relations = listOf(arrangements)
)

val fakeWorks = listOf(
    fakeWorkWithAllData,
    fakeWork2
)

val browseWorksResponse = BrowseWorksResponse(
    count = 1,
    offset = 0,
    listOf(element = fakeWorkWithAllData)
)

val searchWorksResponse = SearchWorksResponse(
    count = 1,
    offset = 0,
    listOf(element = fakeWorkWithAllData)
)
