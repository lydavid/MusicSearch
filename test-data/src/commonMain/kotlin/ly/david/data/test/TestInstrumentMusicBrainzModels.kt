package ly.david.data.test

import ly.david.musicsearch.data.musicbrainz.models.common.AliasMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.InstrumentMusicBrainzNetworkModel

val fiddleInstrumentMusicBrainzModel = InstrumentMusicBrainzNetworkModel(
    id = "04a21d03-535a-4ace-9098-12013867b8e5",
    name = "fiddle",
    disambiguation = "",
    type = "Family",
    typeId = "7c9126a3-32a8-4a46-ba46-b21c83874827",
    description = "Generally any bowed handle lute with the characteristic \"violin\" shape belong to fiddles.",
    aliases = listOf(
        AliasMusicBrainzNetworkModel(
            name = "fiddle",
            isPrimary = true,
            locale = "en",
        ),
        AliasMusicBrainzNetworkModel(
            name = "φιντλ",
            isPrimary = true,
            locale = "el",
        ),
        // and more
    ),
)

val violinInstrumentMusicBrainzModel = InstrumentMusicBrainzNetworkModel(
    id = "089f123c-0f7d-4105-a64e-49de81ca8fa4",
    name = "violin",
    disambiguation = "Soprano of modern violin family",
    type = "String instrument",
    typeId = "cc00f97f-cf3d-3ae2-9163-041cb1a0d726",
    description = "The most famous member of the violin family, it is actually the \"small viol\". Its register is soprano and it's a principal member of the symphony orchestra.",
    aliases = listOf(
        AliasMusicBrainzNetworkModel(
            name = "violin",
            isPrimary = true,
            locale = "en",
        ),
        AliasMusicBrainzNetworkModel(
            name = "violín",
            isPrimary = true,
            locale = "es",
        ),
        // and more
    ),
)
