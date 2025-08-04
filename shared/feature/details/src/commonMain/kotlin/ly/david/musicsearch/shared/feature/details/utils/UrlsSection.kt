package ly.david.musicsearch.shared.feature.details.utils

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.ui.common.listitem.CollapsibleListSeparatorHeader
import ly.david.musicsearch.ui.common.relation.UrlListItem

internal fun LazyListScope.urlsSection(
    urls: List<RelationListItemModel>,
    collapsed: Boolean = false,
    onCollapseExpand: () -> Unit = {},
) {
    urls.ifNotNullOrEmpty {
        item {
            CollapsibleListSeparatorHeader(
                text = "External links",
                collapsed = collapsed,
                onClick = onCollapseExpand,
            )
        }
    }
    if (!collapsed) {
        items(urls) {
            UrlListItem(
                relation = it,
            )
        }
    }
}
