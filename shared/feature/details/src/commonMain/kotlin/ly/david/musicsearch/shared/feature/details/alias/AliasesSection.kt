package ly.david.musicsearch.shared.feature.details.alias

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.feature.details.utils.getNumberOfFilteredItems
import ly.david.musicsearch.ui.common.listitem.CollapsibleListSeparatorHeader
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.aliases
import org.jetbrains.compose.resources.stringResource

internal fun LazyListScope.aliasesSection(
    aliases: ImmutableList<AliasListItemModel>,
    primaryLabel: String,
    filterText: String,
    collapsed: Boolean = false,
    onCollapseExpand: () -> Unit = {},
) {
    val filteredAliases = aliases.filterAliases(
        query = filterText,
        primaryLabel = primaryLabel,
    )
    filteredAliases.ifNotNullOrEmpty {
        stickyHeader {
            val numberOfFilteredItems = getNumberOfFilteredItems(
                filteredCount = filteredAliases.size,
                total = aliases.size,
            )
            CollapsibleListSeparatorHeader(
                text = stringResource(Res.string.aliases) + " $numberOfFilteredItems",
                collapsed = collapsed,
                onClick = onCollapseExpand,
            )
        }
    }
    if (!collapsed) {
        items(filteredAliases) { alias ->
            AliasListItem(
                alias = alias,
                filterText = filterText,
            )
        }
    }
}

private fun ImmutableList<AliasListItemModel>?.filterAliases(
    query: String,
    primaryLabel: String,
): ImmutableList<AliasListItemModel> {
    val searchText = query.lowercase()
    return this?.filter { alias ->
        listOf(
            alias.name,
            alias.getFormattedTypeAndLifeSpan(primaryLabel),
        ).any { it.lowercase().contains(searchText) }
    }.orEmpty().toPersistentList()
}
