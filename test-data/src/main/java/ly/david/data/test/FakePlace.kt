package ly.david.data.test

import ly.david.data.CoordinatesMusicBrainzModel
import ly.david.data.LifeSpanMusicBrainzModel
import ly.david.data.network.Direction
import ly.david.data.network.MusicBrainzEntity
import ly.david.data.network.PlaceMusicBrainzModel
import ly.david.data.network.RelationMusicBrainzModel
import ly.david.data.network.api.BrowsePlacesResponse
import ly.david.data.network.api.SearchPlacesResponse

val fakePlace = PlaceMusicBrainzModel(
    id = "place1",
    name = "Place Name",
    type = "Venue"
)

val eventHeldAtPlace = RelationMusicBrainzModel(
    type = "doesn't matter",
    typeId = "e2c6f697-07dc-38b1-be0b-83d740165532",
    direction = Direction.BACKWARD,
    targetType = MusicBrainzEntity.EVENT,
    event = fakeEvent
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
        ended = true
    ),
    area = ontario,
    coordinates = CoordinatesMusicBrainzModel(
        longitude = 123.123,
        latitude = 123.123
    ),
    relations = listOf(
        eventHeldAtPlace
    )
)

val fakePlaces = listOf(
    fakePlace,
    fakePlaceWithAllData,
)

val browsePlacesResponse = BrowsePlacesResponse(
    count = 1,
    offset = 0,
    musicBrainzModels = listOf(fakePlace)
)

val searchPlacesResponse = SearchPlacesResponse(
    count = 1,
    offset = 0,
    listOf(element = fakePlace)
)
