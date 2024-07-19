package ly.david.data.test

import ly.david.musicsearch.data.musicbrainz.api.BrowseWorksResponse
import ly.david.musicsearch.data.musicbrainz.api.SearchWorksResponse
import ly.david.musicsearch.data.musicbrainz.models.core.WorkAttributeMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.WorkMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.relation.Direction
import ly.david.musicsearch.data.musicbrainz.models.relation.RelatableMusicBrainzEntity
import ly.david.musicsearch.data.musicbrainz.models.relation.RelationMusicBrainzModel

val fakeWorkAttribute = WorkAttributeMusicBrainzModel(
    type = "SUISA ID",
    typeId = "034f35ae-d250-3749-95e7-854e606d5960",
    value = "000321 768 01",
)

val fakeWork2 = WorkMusicBrainzModel(
    id = "w2",
    name = "work 2",
)

val arrangements = RelationMusicBrainzModel(
    type = "doesn't matter",
    typeId = "51975ed8-bbfa-486b-9f28-5947f4370299",
    direction = Direction.FORWARD,
    targetType = RelatableMusicBrainzEntity.WORK,
    work = fakeWork2,
)

val fakeWorkWithAllData = WorkMusicBrainzModel(
    id = "w1",
    name = "Work Name",
    type = "Song",
    attributes = listOf(
        fakeWorkAttribute,
    ),
    relations = listOf(arrangements),
)

val fakeWorks = listOf(
    fakeWorkWithAllData,
    fakeWork2,
)

val browseWorksResponse = BrowseWorksResponse(
    count = 1,
    offset = 0,
    listOf(element = fakeWorkWithAllData),
)

val searchWorksResponse = SearchWorksResponse(
    count = 1,
    offset = 0,
    listOf(element = fakeWorkWithAllData),
)
