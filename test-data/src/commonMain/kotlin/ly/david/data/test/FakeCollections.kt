package ly.david.data.test

import ly.david.musicsearch.data.musicbrainz.models.core.CollectionMusicBrainzModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.data.musicbrainz.api.BrowseCollectionsResponse

val fakeAreaCollection = CollectionMusicBrainzModel(
    id = "fakeAreaCollection",
    name = "My areas",
    entity = MusicBrainzEntity.AREA,
)

val fakeArtistCollection = CollectionMusicBrainzModel(
    id = "fakeArtistCollection",
    name = "My artists",
    entity = MusicBrainzEntity.ARTIST,
)

val fakeCollections = listOf(
    fakeAreaCollection,
    fakeArtistCollection,
)

val browseCollectionsResponse = BrowseCollectionsResponse(
    count = 1,
    offset = 0,
    musicBrainzModels = fakeCollections,
)
