package ly.david.musicsearch.shared.feature.details.utils

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import kotlinx.collections.immutable.ImmutableList
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.ui.common.listitem.CollapsibleListSeparatorHeader
import ly.david.musicsearch.ui.common.relation.UrlListItem

internal fun LazyListScope.urlsSection(
    filteredUrls: ImmutableList<RelationListItemModel>,
    totalUrls: Int,
    collapsed: Boolean = false,
    onCollapseExpand: () -> Unit = {},
) {
    filteredUrls.ifNotNullOrEmpty {
        stickyHeader {
            val numberOfFilteredItems = getNumberOfFilteredItems(
                filteredCount = filteredUrls.size,
                total = totalUrls,
            )
            CollapsibleListSeparatorHeader(
                text = "External links $numberOfFilteredItems",
                collapsed = collapsed,
                onClick = onCollapseExpand,
            )
        }
    }
    if (!collapsed) {
        items(filteredUrls) {
            UrlListItem(
                relation = it,
            )
        }
    }
}
