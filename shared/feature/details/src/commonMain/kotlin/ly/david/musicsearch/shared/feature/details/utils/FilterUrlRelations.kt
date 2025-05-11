package ly.david.musicsearch.shared.feature.details.utils

import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel

internal fun List<RelationListItemModel>?.filterUrlRelations(query: String): List<RelationListItemModel> {
    return this?.filter { relationListItemModel ->
        val searchText = query.lowercase()
        listOf(
            relationListItemModel.name,
            relationListItemModel.label,
        ).any { it.lowercase().contains(searchText) }
    }.orEmpty()
}
