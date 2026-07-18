package ly.david.data.test

import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.shared.domain.instrument.InstrumentType
import ly.david.musicsearch.shared.domain.listitem.InstrumentListItemModel

val fiddleInstrumentListItemModel = InstrumentListItemModel(
    id = "04a21d03-535a-4ace-9098-12013867b8e5",
    name = "fiddle",
    disambiguation = "",
    type = InstrumentType.Family,
    description = "Generally any bowed handle lute with the characteristic \"violin\" shape belong to fiddles.",
    aliases = persistentListOf(
        BasicAlias(
            name = "fiddle",
            isPrimary = true,
            locale = "en",
        ),
        BasicAlias(
            name = "φιντλ",
            isPrimary = true,
            locale = "el",
        ),
        // and more
    ),
)

val violinInstrumentListItemModel = InstrumentListItemModel(
    id = "089f123c-0f7d-4105-a64e-49de81ca8fa4",
    name = "violin",
    disambiguation = "Soprano of modern violin family",
    type = InstrumentType.StringInstrument,
    description = "The most famous member of the violin family, it is actually the \"small viol\"." +
        " Its register is soprano and it's a principal member of the symphony orchestra.",
    aliases = persistentListOf(
        BasicAlias(
            name = "violin",
            isPrimary = true,
            locale = "en",
        ),
        BasicAlias(
            name = "violín",
            isPrimary = true,
            locale = "es",
        ),
        // and more
    ),
)
