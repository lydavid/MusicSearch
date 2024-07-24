package ly.david.data.test

import ly.david.musicsearch.data.musicbrainz.api.BrowsePlacesResponse
import ly.david.musicsearch.data.musicbrainz.api.SearchPlacesResponse
import ly.david.musicsearch.data.musicbrainz.models.common.LifeSpanMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.CoordinatesMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.PlaceMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.relation.Direction
import ly.david.musicsearch.data.musicbrainz.models.relation.SerializableMusicBrainzEntity
import ly.david.musicsearch.data.musicbrainz.models.relation.RelationMusicBrainzModel

val fakePlace = PlaceMusicBrainzModel(
    id = "place1",
    name = "Place Name",
    type = "Venue",
)

val eventHeldAtPlace = RelationMusicBrainzModel(
    type = "doesn't matter",
    typeId = "e2c6f697-07dc-38b1-be0b-83d740165532",
    direction = Direction.BACKWARD,
    targetType = SerializableMusicBrainzEntity.EVENT,
    event = fakeEvent,
)

val fakePlaceWithAllData = PlaceMusicBrainzModel(
    id = "place2",
    name = "Place With All Data",
    disambiguation = "that one",
    address = "123 Fake St",
    type = "Studio",
    lifeSpan = LifeSpanMusicBrainzModel(
        begin = "2022-01-01",
        end = "2022-12-10",
        ended = true,
    ),
    area = ontario,
    coordinates = CoordinatesMusicBrainzModel(
        longitude = 123.123,
        latitude = 123.123,
    ),
    relations = listOf(
        eventHeldAtPlace,
    ),
)

val fakePlaces = listOf(
    fakePlace,
    fakePlaceWithAllData,
)

val browsePlacesResponse = BrowsePlacesResponse(
    count = 1,
    offset = 0,
    musicBrainzModels = listOf(fakePlace),
)

val searchPlacesResponse = SearchPlacesResponse(
    count = 1,
    offset = 0,
    listOf(element = fakePlace),
)
