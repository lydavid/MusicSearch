package ly.david.data.test

import ly.david.data.core.AreaType
import ly.david.data.network.AreaMusicBrainzModel
import ly.david.data.network.Direction
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.network.RelationMusicBrainzModel
import ly.david.data.network.api.BrowseAreasResponse
import ly.david.data.network.api.SearchAreasResponse

val canada = AreaMusicBrainzModel(
    id = "71bbafaa-e825-3e15-8ca9-017dcad1748b",
    name = "Canada",
    type = AreaType.COUNTRY
)

val toronto = AreaMusicBrainzModel(
    id = "74b24e62-d2fe-42d2-9d96-31f2da756c77",
    name = "Toronto",
    type = "City"
)

/**
 * All fake models should start as MusicBrainzModel, since we can convert them to RoomModel and UiModel.
 */
val ontario = AreaMusicBrainzModel(
    id = "fd3d44c5-80a1-3842-9745-2c4972d35afa",
    name = "Ontario",
    type = "Subdivision",
    relations = listOf(
        RelationMusicBrainzModel(
            type = "part of",
            typeId = "de7cc874-8b1b-3a05-8272-f3834c968fb7",
            direction = Direction.BACKWARD,
            targetType = MusicBrainzEntity.AREA,
            area = canada
        ),
        RelationMusicBrainzModel(
            type = "part of",
            typeId = "de7cc874-8b1b-3a05-8272-f3834c968fb7",
            direction = Direction.FORWARD,
            targetType = MusicBrainzEntity.AREA,
            area = toronto
        ),
    )
)

val fakeAreas = listOf(
    ontario,
    canada
)

val browseAreasResponse = BrowseAreasResponse(
    count = 1,
    offset = 0,
    musicBrainzModels = listOf(ontario)
)

val searchAreasResponse = SearchAreasResponse(
    count = 1,
    offset = 0,
    areas = listOf(element = ontario)
)
