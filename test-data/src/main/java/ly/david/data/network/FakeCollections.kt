package ly.david.data.network

import ly.david.data.network.api.BrowseCollectionsResponse

val fakeCollections = listOf(
    CollectionMusicBrainzModel(
        id = "a",
        name = "My areas",
        entity = MusicBrainzResource.AREA
    )
)

val browseCollectionsResponse = BrowseCollectionsResponse(
    count = 1,
    offset = 0,
    musicBrainzModels = fakeCollections
)
