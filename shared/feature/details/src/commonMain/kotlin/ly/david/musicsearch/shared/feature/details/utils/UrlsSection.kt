package ly.david.musicsearch.shared.feature.details.utils

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import kotlinx.collections.immutable.ImmutableList
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.ui.common.listitem.CollapsibleListSeparatorHeader
import ly.david.musicsearch.ui.common.relation.UrlListItem
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.externalLinks
import org.jetbrains.compose.resources.stringResource

internal fun LazyListScope.urlsSection(
    urls: ImmutableList<RelationListItemModel>,
    filterText: String,
    collapsed: Boolean = false,
    onCollapseExpand: () -> Unit = {},
) {
    val filteredUrls = urls.filterUrlRelations(query = filterText)
    filteredUrls.ifNotNullOrEmpty {
        stickyHeader {
            val numberOfFilteredItems = getNumberOfFilteredItems(
                filteredCount = filteredUrls.size,
                total = urls.size,
            )
            CollapsibleListSeparatorHeader(
                text = stringResource(Res.string.externalLinks) + " $numberOfFilteredItems",
                collapsed = collapsed,
                onClick = onCollapseExpand,
            )
        }
    }
    if (!collapsed) {
        items(filteredUrls) {
            UrlListItem(
                relation = it,
                filterText = filterText,
            )
        }
    }
}
