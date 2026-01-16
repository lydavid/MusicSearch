package ly.david.musicsearch.data.database.mapper

import kotlinx.collections.immutable.toPersistentList
import ly.david.musicsearch.data.database.UNKNOWN_LISTENS_FLAG
import ly.david.musicsearch.shared.domain.listitem.WorkListItemModel

fun mapToWorkListItemModel(
    id: String,
    name: String,
    disambiguation: String,
    type: String,
    languages: List<String>,
    iswcs: List<String>,
    visited: Boolean?,
    collected: Boolean?,
    aliasNames: String?,
    aliasLocales: String?,
    listenCount: Long?,
) = WorkListItemModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
    type = type,
    languages = languages.toPersistentList(),
    iswcs = iswcs.toPersistentList(),
    visited = visited == true,
    collected = collected == true,
    aliases = combineToAliases(aliasNames, aliasLocales),
    listenState = toListenState(listenCount),
)

private fun toListenState(
    listenCount: Long?,
) = when (listenCount) {
    null -> WorkListItemModel.ListenState.Hide
    UNKNOWN_LISTENS_FLAG -> WorkListItemModel.ListenState.Unknown
    else -> WorkListItemModel.ListenState.Known(listenCount)
}
