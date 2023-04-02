package ly.david.data.network

import ly.david.data.network.api.BrowseCollectionsResponse

val fakeAreaCollection = CollectionMusicBrainzModel(
    id = "fakeAreaCollection",
    name = "My areas",
    entity = MusicBrainzResource.AREA
)

val fakeArtistCollection = CollectionMusicBrainzModel(
    id = "fakeArtistCollection",
    name = "My artists",
    entity = MusicBrainzResource.ARTIST
)

val fakeCollections = listOf(
    fakeAreaCollection,
    fakeArtistCollection
)

val browseCollectionsResponse = BrowseCollectionsResponse(
    count = 1,
    offset = 0,
    musicBrainzModels = fakeCollections
)
