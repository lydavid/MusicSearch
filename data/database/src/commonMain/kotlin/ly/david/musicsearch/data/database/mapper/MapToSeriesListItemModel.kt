package ly.david.musicsearch.data.database.mapper

import ly.david.musicsearch.shared.domain.listitem.SeriesListItemModel
import ly.david.musicsearch.shared.domain.series.SeriesType

fun mapToSeriesListItemModel(
    id: String,
    name: String,
    disambiguation: String,
    type: String,
    visited: Boolean?,
    collected: Boolean?,
    aliasNames: String?,
    aliasLocales: String?,
) = SeriesListItemModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
    type = SeriesType.fromName(type),
    visited = visited == true,
    collected = collected == true,
    aliases = combineToAliases(aliasNames, aliasLocales),
)
