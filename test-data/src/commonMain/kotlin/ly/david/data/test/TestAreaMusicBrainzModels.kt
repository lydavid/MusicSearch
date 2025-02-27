package ly.david.data.test

import ly.david.musicsearch.data.musicbrainz.models.core.AreaMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.relation.Direction
import ly.david.musicsearch.data.musicbrainz.models.relation.RelationMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.relation.SerializableMusicBrainzEntity
import ly.david.musicsearch.shared.domain.area.AreaType

val canadaAreaMusicBrainzModel = AreaMusicBrainzModel(
    id = "71bbafaa-e825-3e15-8ca9-017dcad1748b",
    name = "Canada",
    sortName = "Canada",
    disambiguation = "",
    type = AreaType.COUNTRY,
    countryCodes = listOf("CA"),
)

val torontoAreaMusicBrainzModel = AreaMusicBrainzModel(
    id = "74b24e62-d2fe-42d2-9d96-31f2da756c77",
    name = "Toronto",
    type = "City",
)

val ontarioAreaMusicBrainzModel = AreaMusicBrainzModel(
    id = "fd3d44c5-80a1-3842-9745-2c4972d35afa",
    name = "Ontario",
    type = "Subdivision",
    relations = listOf(
        RelationMusicBrainzModel(
            type = "part of",
            typeId = "de7cc874-8b1b-3a05-8272-f3834c968fb7",
            direction = Direction.BACKWARD,
            targetType = SerializableMusicBrainzEntity.AREA,
            area = canadaAreaMusicBrainzModel,
        ),
        RelationMusicBrainzModel(
            type = "part of",
            typeId = "de7cc874-8b1b-3a05-8272-f3834c968fb7",
            direction = Direction.FORWARD,
            targetType = SerializableMusicBrainzEntity.AREA,
            area = torontoAreaMusicBrainzModel,
        ),
    ),
)

val japanAreaMusicBrainzModel = AreaMusicBrainzModel(
    id = "2db42837-c832-3c27-b4a3-08198f75693c",
    name = "Japan",
    sortName = "Japan",
    disambiguation = "",
    countryCodes = listOf("JP"),
    type = AreaType.COUNTRY,
)
