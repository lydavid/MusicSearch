package ly.david.musicsearch.shared.feature.details.alias

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import kotlinx.collections.immutable.ImmutableList
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.feature.details.utils.getNumberOfFilteredItems
import ly.david.musicsearch.ui.common.listitem.CollapsibleListSeparatorHeader

internal fun LazyListScope.aliasesSection(
    filteredAliases: ImmutableList<BasicAlias>,
    totalAliases: Int,
    collapsed: Boolean = false,
    onCollapseExpand: () -> Unit = {},
) {
    filteredAliases.ifNotNullOrEmpty {
        stickyHeader {
            val numberOfFilteredItems = getNumberOfFilteredItems(
                filteredCount = filteredAliases.size,
                total = totalAliases,
            )
            CollapsibleListSeparatorHeader(
                text = "Aliases $numberOfFilteredItems",
                collapsed = collapsed,
                onClick = onCollapseExpand,
            )
        }
    }
    if (!collapsed) {
        items(filteredAliases) { alias ->
            AliasListItem(
                alias = alias,
            )
        }
    }
}
