package ly.david.musicsearch.shared.feature.details.utils

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel

internal fun ImmutableList<RelationListItemModel>?.filterUrlRelations(
    query: String,
): ImmutableList<RelationListItemModel> {
    val searchText = query.lowercase()
    return this?.filter { relationListItemModel ->
        listOf(
            relationListItemModel.name,
            relationListItemModel.type,
        ).any { it.lowercase().contains(searchText) }
    }.orEmpty().toPersistentList()
}
