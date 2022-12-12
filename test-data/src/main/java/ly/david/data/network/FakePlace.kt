package ly.david.data.network

import ly.david.data.Coordinates
import ly.david.data.LifeSpan
import ly.david.data.network.api.BrowsePlacesResponse

val fakePlace = PlaceMusicBrainzModel(
    id = "place1",
    name = "Place Name",
    type = "Venue"
)

val fakePlaceWithAllData = PlaceMusicBrainzModel(
    id = "place2",
    name = "Place With All Data",
    disambiguation = "that one",
    address = "123 Fake St",
    type = "Studio",
    lifeSpan = LifeSpan(
        begin = "2022-01-01",
        end = "2022-12-10",
        ended = true
    ),
    area = fakeArea,
    coordinates = Coordinates(
        longitude = 123.123,
        latitude = 123.123
    )
)

val eventHeldAtPlace = RelationMusicBrainzModel(
    type = "doesn't matter",
    typeId = "e2c6f697-07dc-38b1-be0b-83d740165532",
    direction = Direction.BACKWARD,
    targetType = MusicBrainzResource.EVENT,
    event = fakeEvent
)

val fakePlaceWithRelation = PlaceMusicBrainzModel(
    id = "place3",
    name = "Place With Relation",
    relations = listOf(
        eventHeldAtPlace
    )
)

val fakePlaces = listOf(
    fakePlace,
    fakePlaceWithAllData,
    fakePlaceWithRelation
)

val browsePlacesResponse = BrowsePlacesResponse(
    count = 1,
    offset = 0,
    places = listOf(fakePlace)
)
