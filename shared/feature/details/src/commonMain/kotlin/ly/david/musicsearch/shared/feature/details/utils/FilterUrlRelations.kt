package ly.david.musicsearch.shared.feature.details.utils

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel

internal fun ImmutableList<RelationListItemModel>?.filterUrlRelations(
    query: String,
): ImmutableList<RelationListItemModel> {
    return this?.filter { relationListItemModel ->
        val searchText = query.lowercase()
        listOf(
            relationListItemModel.name,
            relationListItemModel.type,
        ).any { it.lowercase().contains(searchText) }
    }.orEmpty().toPersistentList()
}
