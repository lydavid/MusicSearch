package ly.david.data.test

import ly.david.musicsearch.data.musicbrainz.Direction
import ly.david.musicsearch.data.musicbrainz.LabelInfo
import ly.david.musicsearch.data.musicbrainz.LabelMusicBrainzModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.data.musicbrainz.RelationMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.api.BrowseLabelsResponse
import ly.david.musicsearch.data.musicbrainz.api.SearchLabelsResponse

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
            targetType = MusicBrainzEntity.LABEL,
            label = elektraMusicGroup,
        ),
    ),
)

val underPressureLabelInfo = LabelInfo(
    catalogNumber = "E47235",
    label = elektra,
)

val fakeLabels = listOf(
    elektra,
    elektraMusicGroup,
)

val browseLabelsResponse = BrowseLabelsResponse(
    count = 1,
    offset = 0,
    listOf(element = elektra),
)

val searchLabelsResponse = SearchLabelsResponse(
    count = 1,
    offset = 0,
    listOf(element = elektra),
)
