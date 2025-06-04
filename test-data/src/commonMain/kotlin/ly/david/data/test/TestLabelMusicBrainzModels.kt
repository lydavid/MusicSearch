package ly.david.data.test

import ly.david.musicsearch.data.musicbrainz.models.common.LifeSpanMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.LabelInfo
import ly.david.musicsearch.data.musicbrainz.models.core.LabelMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.relation.Direction
import ly.david.musicsearch.data.musicbrainz.models.relation.SerializableMusicBrainzEntity
import ly.david.musicsearch.data.musicbrainz.models.relation.RelationMusicBrainzModel

val elektraMusicGroupLabelMusicBrainzModel = LabelMusicBrainzNetworkModel(
    id = "c85c94c0-f83b-42f5-bac0-6b89720de387",
    name = "Elektra Music Group",
    type = "Holding",
)

val elektraLabelMusicBrainzModel = LabelMusicBrainzNetworkModel(
    id = "873f9f75-af68-4872-98e2-431058e4c9a9",
    name = "Elektra",
    labelCode = 192,
    type = "Imprint",
    relations = listOf(
        RelationMusicBrainzModel(
            type = "imprint",
            typeId = "23f8c592-006d-4214-9080-c4e5000c05d7",
            direction = Direction.BACKWARD,
            targetType = SerializableMusicBrainzEntity.LABEL,
            label = elektraMusicGroupLabelMusicBrainzModel,
        ),
    ),
)

val underPressureLabelInfo = LabelInfo(
    catalogNumber = "E47235",
    label = elektraLabelMusicBrainzModel,
)

val virginMusicLabelMusicBrainzModel = LabelMusicBrainzNetworkModel(
    id = "7689c51f-e09e-4e85-80d0-b95a9e23d216",
    name = "Virgin Music",
    disambiguation = "a division of Universal Music Japan created in 2014 that replaces EMI R",
    type = "Original Production",
    typeId = "7aaa37fe-2def-3476-b359-80245850062d",
    labelCode = null,
)

val flyingDogLabelMusicBrainzModel = LabelMusicBrainzNetworkModel(
    id = "6574b05f-4825-435f-89f6-bb205da16f3a",
    name = "flying DOG",
    disambiguation = "Victor",
    type = "Original Production",
    typeId = "7aaa37fe-2def-3476-b359-80245850062d",
    labelCode = null,
    lifeSpan = LifeSpanMusicBrainzModel(
        begin = "2007-10-01",
        ended = false,
    ),
    area = japanAreaMusicBrainzModel,
)

val mercuryRecordsLabelMusicBrainzModel = LabelMusicBrainzNetworkModel(
    id = "995428e7-81b6-41dd-bd38-5a7a0ece8ad6",
    name = "Mercury Records",
    type = "Imprint",
    typeId = "b6285b2a-3514-3d43-80df-fcf528824ded",
    labelCode = 268,
    disambiguation = "or just “Mercury.” A UMG imprint, do not use it for ©/℗ credits",
)
