package ly.david.musicsearch.data.database.mapper

import ly.david.musicsearch.shared.domain.listitem.WorkListItemModel

fun mapToWorkListItemModel(
    id: String,
    name: String,
    disambiguation: String?,
    type: String?,
    languages: List<String>?,
    iswcs: List<String>?,
    visited: Boolean?,
    collected: Boolean?,
    aliasNames: String?,
    aliasLocales: String?,
) = WorkListItemModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
    type = type,
    languages = languages.orEmpty(),
    iswcs = iswcs.orEmpty(),
    visited = visited == true,
    collected = collected == true,
    aliases = combineToPrimaryAliases(aliasNames, aliasLocales),
)
