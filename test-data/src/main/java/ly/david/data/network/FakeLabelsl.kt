package ly.david.data.network

import ly.david.data.network.api.BrowseLabelsResponse
import ly.david.data.network.api.SearchLabelsResponse

val fakeLabel = LabelMusicBrainzModel(
    id = "label1",
    name = "Label Name 1",
    labelCode = 123,
    type = "Imprint",
    relations = listOf(
        RelationMusicBrainzModel(
            type = "",
            typeId = "fe16f2bd-d324-435a-8076-bcf43b805bd9",
            direction = Direction.BACKWARD,
            targetType = MusicBrainzResource.ARTIST,
            artist = fakeArtist
        )
    )
)

val fakeLabel2 = LabelMusicBrainzModel(
    id = "label2",
    name = "Label Name 2",
    labelCode = 321,
    type = "Production"
)

val fakeLabelInfo = LabelInfo(
    catalogNumber = "CAT 1",
    label = fakeLabel
)

val fakeLabels = listOf(
    fakeLabel,
    fakeLabel2,
)

val browseLabelsResponse = BrowseLabelsResponse(
    count = 1,
    offset = 0,
    listOf(element = fakeLabel)
)

val searchLabelsResponse = SearchLabelsResponse(
    count = 1,
    offset = 0,
    listOf(element = fakeLabel)
)
