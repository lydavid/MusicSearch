package ly.david.data.network

val fakeWorkAttribute = WorkAttributeMusicBrainzModel(
    type = "SUISA ID",
    typeId = "034f35ae-d250-3749-95e7-854e606d5960",
    value = "000321 768 01"
)

val fakeWork = WorkMusicBrainzModel(
    id = "w1",
    name = "Work Name",
    type = "Song",
    attributes = listOf(
        fakeWorkAttribute
    )
)
