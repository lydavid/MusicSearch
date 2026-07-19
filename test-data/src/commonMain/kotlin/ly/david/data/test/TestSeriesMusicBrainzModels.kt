package ly.david.data.test

import ly.david.musicsearch.data.musicbrainz.models.common.AliasMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.SeriesMusicBrainzNetworkModel

val worksOfBeethovenSeriesMusicBrainzModel = SeriesMusicBrainzNetworkModel(
    id = "004f17aa-0f46-4fec-b939-c5dfc849e5c6",
    name = "Works of Ludwig van Beethoven by opus number",
    disambiguation = "",
    type = "Catalogue",
    typeId = "49482ff0-fc9e-3b8c-a2d0-30e84d9df002",
)

val comiketSeriesMusicBrainzModel = SeriesMusicBrainzNetworkModel(
    id = "ce7866d3-2295-4010-aa3e-40e2a36bda40",
    name = "コミックマーケット",
    disambiguation = "",
    type = "Event series",
    typeId = "64640a2a-1c31-394d-b7b0-683a32ff9aff",
    aliases = listOf(
        AliasMusicBrainzNetworkModel(
            name = "Comic Market",
            isPrimary = true,
            locale = "en",
        ),
        AliasMusicBrainzNetworkModel(
            name = "コミックマーケット",
            isPrimary = true,
            locale = "ja",
        ),
        // and more
    ),
)
