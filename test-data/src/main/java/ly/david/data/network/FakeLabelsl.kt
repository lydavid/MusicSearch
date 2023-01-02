package ly.david.data.network

import ly.david.data.network.api.SearchLabelsResponse

val fakeLabel = LabelMusicBrainzModel(
    id = "label1",
    name = "Label Name 1",
    labelCode = 123,
    type = "Imprint"
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

val searchLabelsResponse = SearchLabelsResponse(
    count = 1,
    offset = 0,
    listOf(element = fakeLabel)
)
