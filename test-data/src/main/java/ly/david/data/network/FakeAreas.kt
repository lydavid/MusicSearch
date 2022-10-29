package ly.david.data.network

import ly.david.data.AreaType

/**
 * All fake models should start as MusicBrainzModel, since we can convert them to RoomModel and UiModel.
 */
val fakeArea = AreaMusicBrainzModel(
    id = "area1", // Note that MB id is unique between all resources (ie. area and artist cannot have the same id)
    name = "Area Name"
)

val fakeCountry = AreaMusicBrainzModel(
    id = "area2",
    name = "Country Name",
    type = AreaType.COUNTRY
)

val areaPartOfArea = RelationMusicBrainzModel(
    type = "doesn't matter",
    typeId = "de7cc874-8b1b-3a05-8272-f3834c968fb7",
    direction = Direction.BACKWARD,
    targetType = MusicBrainzResource.AREA,
    area = fakeCountry
)

val fakeAreaWithRelation = AreaMusicBrainzModel(
    id = "area3",
    name = "Area With Relation",
    relations = listOf(
        areaPartOfArea
    )
)

val fakeAreas = listOf(
    fakeArea,
    fakeCountry,
    fakeAreaWithRelation
)
