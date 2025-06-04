package ly.david.data.test

import ly.david.musicsearch.data.musicbrainz.api.BrowseCollectionsResponse
import ly.david.musicsearch.data.musicbrainz.models.core.CollectionMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.relation.SerializableMusicBrainzEntity

val fakeAreaCollection = CollectionMusicBrainzNetworkModel(
    id = "fakeAreaCollection",
    name = "My areas",
    entityType = SerializableMusicBrainzEntity.AREA,
)

val fakeArtistCollection = CollectionMusicBrainzNetworkModel(
    id = "fakeArtistCollection",
    name = "My artists",
    entityType = SerializableMusicBrainzEntity.ARTIST,
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
