package ly.david.data.test

import ly.david.data.network.CollectionMusicBrainzModel
import ly.david.data.network.MusicBrainzEntity
import ly.david.data.network.api.BrowseCollectionsResponse

val fakeAreaCollection = CollectionMusicBrainzModel(
    id = "fakeAreaCollection",
    name = "My areas",
    entity = MusicBrainzEntity.AREA
)

val fakeArtistCollection = CollectionMusicBrainzModel(
    id = "fakeArtistCollection",
    name = "My artists",
    entity = MusicBrainzEntity.ARTIST
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
