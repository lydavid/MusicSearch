package ly.david.data.test

import ly.david.musicsearch.data.musicbrainz.models.core.LabelInfo
import ly.david.musicsearch.data.musicbrainz.models.core.LabelMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.relation.Direction
import ly.david.musicsearch.data.musicbrainz.models.relation.SerializableMusicBrainzEntity
import ly.david.musicsearch.data.musicbrainz.models.relation.RelationMusicBrainzModel

val elektraMusicGroup = LabelMusicBrainzModel(
    id = "c85c94c0-f83b-42f5-bac0-6b89720de387",
    name = "Elektra Music Group",
    type = "Holding",
)

val elektra = LabelMusicBrainzModel(
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
            label = elektraMusicGroup,
        ),
    ),
)

val underPressureLabelInfo = LabelInfo(
    catalogNumber = "E47235",
    label = elektra,
)

val virginMusicLabelMusicBrainzModel = LabelMusicBrainzModel(
    id = "7689c51f-e09e-4e85-80d0-b95a9e23d216",
    name = "Virgin Music",
    disambiguation = "a division of Universal Music Japan created in 2014 that replaces EMI R",
    type = "Original Production",
    typeId = "7aaa37fe-2def-3476-b359-80245850062d",
    labelCode = null,
)
