package ly.david.data.test

import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.shared.domain.listitem.SeriesListItemModel
import ly.david.musicsearch.shared.domain.series.SeriesType

val worksOfBeethovenSeriesListItemModel = SeriesListItemModel(
    id = "004f17aa-0f46-4fec-b939-c5dfc849e5c6",
    name = "Works of Ludwig van Beethoven by opus number",
    disambiguation = "",
    type = SeriesType.Catalogue,
)

val comiketSeriesListItemModel = SeriesListItemModel(
    id = "ce7866d3-2295-4010-aa3e-40e2a36bda40",
    name = "コミックマーケット",
    disambiguation = "",
    type = SeriesType.EventSeries,
    aliases = persistentListOf(
        BasicAlias(
            name = "Comic Market",
            isPrimary = true,
            locale = "en",
        ),
        BasicAlias(
            name = "コミックマーケット",
            isPrimary = true,
            locale = "ja",
        ),
        // and more
    ),
)
