package ly.david.musicsearch.data.database.mapper

import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.listitem.LabelListItemModel

fun mapToLabelListItemModel(
    id: String,
    name: String,
    disambiguation: String,
    type: String,
    labelCode: Int?,
    begin: String,
    end: String,
    visited: Boolean?,
    collected: Boolean?,
    aliasNames: String?,
    aliasLocales: String?,
) = LabelListItemModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
    type = type,
    labelCode = labelCode,
    lifeSpan = LifeSpanUiModel(
        begin = begin,
        end = end,
        ended = end.isNotEmpty(),
    ),
    visited = visited == true,
    collected = collected == true,
    aliases = combineToAliases(aliasNames, aliasLocales),
)

fun mapToLabelListItemModel(
    id: String,
    name: String,
    disambiguation: String,
    type: String,
    labelCode: Int?,
    catalogNumbers: String?,
    visited: Boolean?,
    aliasNames: String?,
    aliasLocales: String?,
) = LabelListItemModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
    type = type,
    labelCode = labelCode,
    catalogNumbers = catalogNumbers,
    visited = visited == true,
    aliases = combineToAliases(aliasNames, aliasLocales),
)
